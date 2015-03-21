package com.almondtools.comtemplate.parser;

import static com.almondtools.comtemplate.engine.TemplateParameter.param;
import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.BooleanLiteral.bool;
import static com.almondtools.comtemplate.engine.expressions.DecimalLiteral.decimal;
import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.comtemplate.engine.expressions.ListLiteral.list;
import static com.almondtools.comtemplate.engine.expressions.MapLiteral.map;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.almondtools.comtemplate.engine.CustomObjectDefinition;
import com.almondtools.comtemplate.engine.CustomTemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateDefinition;
import com.almondtools.comtemplate.engine.TemplateExpression;
import com.almondtools.comtemplate.engine.TemplateGroup;
import com.almondtools.comtemplate.engine.TemplateLoader;
import com.almondtools.comtemplate.engine.TemplateParameter;
import com.almondtools.comtemplate.engine.TemplateVariable;
import com.almondtools.comtemplate.engine.expressions.Concat;
import com.almondtools.comtemplate.engine.expressions.Defaulted;
import com.almondtools.comtemplate.engine.expressions.EvalAnonymousTemplate;
import com.almondtools.comtemplate.engine.expressions.EvalAttribute;
import com.almondtools.comtemplate.engine.expressions.EvalContextVar;
import com.almondtools.comtemplate.engine.expressions.EvalFunction;
import com.almondtools.comtemplate.engine.expressions.EvalTemplate;
import com.almondtools.comtemplate.engine.expressions.EvalTemplateFunction;
import com.almondtools.comtemplate.engine.expressions.EvalVar;
import com.almondtools.comtemplate.engine.expressions.Exists;
import com.almondtools.comtemplate.engine.expressions.RawText;
import com.almondtools.comtemplate.parser.ComtemplateParser.AttributeContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.AttributesContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.BoolScalarContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ClauseContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ClausesContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.CodeChunkContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.DecScalarContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.FunctionClauseContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.FunctionContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ImportctpContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ImportpackageContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ImportruleContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ImportsContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.InlineTextContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.InlineTokenContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.IntScalarContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ItemContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ItemsContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ListContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.MapContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ObjectDefinitionContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ParameterContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ParametersContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.QualifiedNameContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.QualifiedWildcardContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.RefTemplateByNameContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.RefTemplateBySequenceContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.RefTemplateEmptyContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.RefTemplateErrorContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.RefVariableContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.StrScalarContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.TemplateBodyContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.TemplateContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.TemplateDefinitionContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.TemplateFileContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.TemplateReferenceContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.TextChunkContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.TextWhitespaceContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ValueAnonymousTemplateContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ValueAttributeContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ValueClauseContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ValueConcatContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ValueDefaultContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ValueDefinitionContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ValueExistsContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ValueFunctionContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ValueListContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ValueMapContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ValueRefContext;
import com.almondtools.comtemplate.parser.ComtemplateParser.ValueScalarContext;
import com.almondtools.util.stream.Unescaper;

public class TemplateGroupBuilder extends AbstractParseTreeVisitor<TemplateGroupNode> implements ComtemplateVisitor<TemplateGroupNode> {

	private static final char[] ESCAPE_MAPPING_STRING = new char[] { 'b', '\b', 't', '\t', 'n', '\n', 'f', '\f', 'r', '\r', '"', '"', '\'', '\'', '\\', '\\', 'u', 0 };
	private static final char[] ESCAPE_MAPPING_RAWTEXT = new char[] { '}', '}', '{', '{', '#', '#', 'u', 0 };

	private String name;
	private ParserRuleContext root;
	private TemplateLoader loader;

	private TemplateGroup activeGroup;
	private TemplateDefinition activeDefinition;

	public TemplateGroupBuilder(String name, ParserRuleContext root) {
		this(name, root, new UnsupportedLoader());
	}

	public TemplateGroupBuilder(String name, ParserRuleContext root, TemplateLoader loader) {
		this.name = name;
		this.root = root;
		this.loader = loader;
	}

	@Override
	public TemplateGroupNode visitImports(ImportsContext ctx) {
		throw new UnsupportedOperationException("group builder skips visiting imports node");
	}

	@Override
	public TemplateGroupNode visitImportrule(ImportruleContext ctx) {
		String definitionName = ctx.qualifiedName().getText();
		TemplateDefinition definition = loader.loadDefinition(definitionName);
		activeGroup.addImport(definition);
		return node(definition);
	}

	@Override
	public TemplateGroupNode visitImportpackage(ImportpackageContext ctx) {
		String groupName = ctx.qualifiedWildcard().qualifiedName().getText();
		TemplateGroup group = loader.loadGroup(groupName);
		for (TemplateDefinition definition : group.getDefinitions()) {
			activeGroup.addImport(definition);
		}
		return node(group);
	}

	@Override
	public TemplateGroupNode visitQualifiedName(QualifiedNameContext ctx) {
		throw new UnsupportedOperationException("group builder skips visiting qualified name node");
	}

	@Override
	public TemplateGroupNode visitQualifiedWildcard(QualifiedWildcardContext ctx) {
		throw new UnsupportedOperationException("group builder skips visiting qualified wildcard node");
	}

	@Override
	public TemplateGroupNode visitTemplateFile(TemplateFileContext ctx) {
		activeGroup = new TemplateGroup(name);
		for (ImportctpContext imp : ctx.imports().importctp()) {
			imp.accept(this);
		}
		for (ClauseContext clause : ctx.clauses().clause()) {
			clause.accept(this);
		}
		return node(activeGroup);
	}

	@Override
	public TemplateGroupNode visitClauses(ClausesContext ctx) {
		throw new UnsupportedOperationException("group builder skips visiting clauses node");
	}

	@Override
	public TemplateGroupNode visitValueClause(ValueClauseContext ctx) {
		return ctx.valueDefinition().accept(this);
	}

	@Override
	public TemplateGroupNode visitFunctionClause(FunctionClauseContext ctx) {
		return ctx.functionDefinition().accept(this);
	}

	@Override
	public TemplateGroupNode visitValueDefinition(ValueDefinitionContext ctx) {
		String name = ctx.name.getText();
		TemplateExpression value = ctx.value().accept(this).as(TemplateExpression.class);
		activeGroup.defineConstant(var(name, value));
		return node(var(name, value));
	}

	@Override
	public TemplateGroupNode visitTemplateDefinition(TemplateDefinitionContext ctx) {
		String name = ctx.name.getText();
		List<TemplateParameter> templateParameters = opt(ctx.parameters())
			.map(parameters -> parameters.parameter().stream()
				.map(parameter -> parameter.accept(this).as(TemplateParameter.class))
				.collect(toList()))
			.orElse(emptyList());
		CustomTemplateDefinition templateDefinition = activeGroup.defineTemplate(name, templateParameters);
		activeDefinition = templateDefinition;
		EvalAnonymousTemplate template = ctx.template().accept(this).as(EvalAnonymousTemplate.class);
		template.stripEnclosingNewLines();
		for (TemplateExpression expression : template.getExpressions()) {
			templateDefinition.add(expression);
		}
		return node(templateDefinition);
	}

	@Override
	public TemplateGroupNode visitObjectDefinition(ObjectDefinitionContext ctx) {
		String name = ctx.name.getText();
		List<TemplateParameter> templateParameters = opt(ctx.parameters())
			.map(parameters -> parameters.parameter().stream()
				.map(parameter -> parameter.accept(this).as(TemplateParameter.class))
				.collect(toList()))
			.orElse(emptyList());
		CustomObjectDefinition objectDefinition = activeGroup.defineObject(name, templateParameters);
		activeDefinition = objectDefinition;
		TemplateExpression result = ctx.value().accept(this).as(TemplateExpression.class);
		objectDefinition.setResult(result);
		return node(objectDefinition);
	}

	@Override
	public TemplateGroupNode visitTemplate(TemplateContext ctx) {
		List<TemplateExpression> expressions = ctx.templateBody().templateChunk().stream()
			.map(chunk -> chunk.accept(this).as(TemplateExpression.class))
			.collect(toList());
		return node(new EvalAnonymousTemplate(activeDefinition, expressions));
	}

	@Override
	public TemplateGroupNode visitTemplateBody(TemplateBodyContext ctx) {
		throw new UnsupportedOperationException("group builder skips visiting template body node");
	}

	@Override
	public TemplateGroupNode visitCodeChunk(CodeChunkContext ctx) {
		return ctx.templateReference().accept(this);
	}

	@Override
	public TemplateGroupNode visitTextChunk(TextChunkContext ctx) {
		return ctx.inlineText().accept(this);
	}

	@Override
	public TemplateGroupNode visitTextWhitespace(TextWhitespaceContext ctx) {
		return node(new RawText(ctx.getText()));
	}

	@Override
	public TemplateGroupNode visitTemplateReference(TemplateReferenceContext ctx) {
		TemplateExpression value = ctx.value().accept(this).as(TemplateExpression.class);
		return node(value);
	}

	@Override
	public TemplateGroupNode visitParameters(ParametersContext ctx) {
		throw new UnsupportedOperationException("group builder skips visiting parameters node");
	}

	@Override
	public TemplateGroupNode visitParameter(ParameterContext ctx) {
		String name = ctx.name.getText();
		String type = ctx.type != null ? ctx.type.getText() : null;
		TemplateExpression defaultValue = opt(ctx.value())
			.map(value -> value.accept(this).as(TemplateExpression.class))
			.orElse(null);
		return node(param(name, type, defaultValue));
	}

	@Override
	public TemplateGroupNode visitRefVariable(RefVariableContext ctx) {
		boolean inContext = ctx.ReferenceMark() != null;
		String name = ctx.name.getText();
		TemplateExpression expression = inContext ? new EvalContextVar(name) : new EvalVar(name, activeDefinition);
		return node(expression);
	}

	@Override
	public TemplateGroupNode visitRefTemplateByName(RefTemplateByNameContext ctx) {
		String template = ctx.name.getText();
		List<TemplateVariable> templateAssignments = opt(ctx.attributes())
			.map(attributes -> attributes.attribute().stream()
				.map(attribute -> attribute.accept(this).as(TemplateVariable.class))
				.collect(toList()))
			.orElse(emptyList());
		return node(new EvalTemplate(template, activeDefinition, templateAssignments));
	}

	@Override
	public TemplateGroupNode visitRefTemplateBySequence(RefTemplateBySequenceContext ctx) {
		String template = ctx.name.getText();
		List<TemplateExpression> templateItems = opt(ctx.items())
			.map(items -> items.item().stream()
				.map(item -> item.accept(this).as(TemplateExpression.class))
				.collect(toList()))
			.orElse(emptyList());
		return node(new EvalTemplateFunction(template, activeDefinition, templateItems));
	}

	@Override
	public TemplateGroupNode visitRefTemplateEmpty(RefTemplateEmptyContext ctx) {
		String template = ctx.name.getText();
		return node(new EvalTemplateFunction(template, activeDefinition, emptyList()));
	}

	@Override
	public TemplateGroupNode visitMap(MapContext ctx) {
		Map<String, TemplateExpression> map = opt(ctx.attributes())
			.map(attributes -> (Map<String, TemplateExpression>) attributes.attribute().stream()
				.map(attribute -> attribute.accept(this).as(TemplateVariable.class))
				.collect(toMap(TemplateVariable::getName, TemplateVariable::getValue, (o, n) -> n, LinkedHashMap::new)))
			.orElse(emptyMap());
		return node(map(map));
	}

	@Override
	public TemplateGroupNode visitFunction(FunctionContext ctx) {
		throw new UnsupportedOperationException("group builder skips visiting function node");
	}

	@Override
	public TemplateGroupNode visitAttributes(AttributesContext ctx) {
		throw new UnsupportedOperationException("group builder skips visiting attributes node");
	}

	@Override
	public TemplateGroupNode visitAttribute(AttributeContext ctx) {
		String text = ctx.name.getText();
		TemplateExpression value = ctx.value().accept(this).as(TemplateExpression.class);
		return node(var(text, value));
	}

	@Override
	public TemplateGroupNode visitList(ListContext ctx) {
		List<TemplateExpression> listItems = opt(ctx.items())
			.map(items -> items.item().stream()
				.map(item -> item.accept(this).as(TemplateExpression.class))
				.collect(toList()))
			.orElse(emptyList());
		return node(list(listItems));
	}

	@Override
	public TemplateGroupNode visitItems(ItemsContext ctx) {
		throw new UnsupportedOperationException("group builder skips visiting items node");
	}

	@Override
	public TemplateGroupNode visitItem(ItemContext ctx) {
		return ctx.value().accept(this);
	}

	@Override
	public TemplateGroupNode visitValueScalar(ValueScalarContext ctx) {
		return ctx.scalar().accept(this);
	}

	@Override
	public TemplateGroupNode visitValueList(ValueListContext ctx) {
		return ctx.list().accept(this);
	}

	@Override
	public TemplateGroupNode visitValueMap(ValueMapContext ctx) {
		return ctx.map().accept(this);
	}

	@Override
	public TemplateGroupNode visitValueRef(ValueRefContext ctx) {
		return ctx.ref().accept(this);
	}

	@Override
	public TemplateGroupNode visitValueAnonymousTemplate(ValueAnonymousTemplateContext ctx) {
		return ctx.template().accept(this);
	}

	@Override
	public TemplateGroupNode visitValueFunction(ValueFunctionContext ctx) {
		TemplateExpression base = ctx.value().accept(this).as(TemplateExpression.class);
		String function = ctx.function().name.getText();
		List<TemplateExpression> arguments = opt(ctx.function().items())
			.map(items -> items.item().stream()
				.map(item -> item.accept(this).as(TemplateExpression.class))
				.collect(toList()))
			.orElse(emptyList());
		return node(new EvalFunction(base, function, arguments));
	}

	@Override
	public TemplateGroupNode visitValueAttribute(ValueAttributeContext ctx) {
		TemplateExpression base = ctx.value().accept(this).as(TemplateExpression.class);
		String attribute = ctx.attr.getText();
		return node(new EvalAttribute(base, attribute));
	}

	@Override
	public TemplateGroupNode visitValueExists(ValueExistsContext ctx) {
		TemplateExpression expression = ctx.value().accept(this).as(TemplateExpression.class);
		return node(new Exists(expression));
	}

	@Override
	public TemplateGroupNode visitValueDefault(ValueDefaultContext ctx) {
		TemplateExpression expression = ctx.value().get(0).accept(this).as(TemplateExpression.class);
		TemplateExpression defaultExpression = ctx.value().get(1).accept(this).as(TemplateExpression.class);
		return node(new Defaulted(expression, defaultExpression));
	}

	@Override
	public TemplateGroupNode visitValueConcat(ValueConcatContext ctx) {
		List<TemplateExpression> arguments = ctx.value().stream()
			.map(value -> value.accept(this).as(TemplateExpression.class))
			.collect(toList());
		return node(new Concat(arguments));
	}

	@Override
	public TemplateGroupNode visitIntScalar(IntScalarContext ctx) {
		return node(integer(ctx.IntegerLiteral().getText()));
	}

	@Override
	public TemplateGroupNode visitDecScalar(DecScalarContext ctx) {
		return node(decimal(ctx.DecimalLiteral().getText()));
	}

	@Override
	public TemplateGroupNode visitStrScalar(StrScalarContext ctx) {
		String unescaped = extractString(ctx.getText());
		return node(string(unescaped));
	}

	private String extractString(String text) {
		String quotesStripped = text.substring(1, text.length() - 1);
		String unescaped = quotesStripped.chars()
			.collect(() -> new Unescaper(ESCAPE_MAPPING_STRING), (u, c) -> u.consume((char) c), Unescaper::join)
			.toString();
		return unescaped;
	}

	@Override
	public TemplateGroupNode visitBoolScalar(BoolScalarContext ctx) {
		return node(bool(ctx.BooleanLiteral().getText()));
	}

	@Override
	public TemplateGroupNode visitInlineText(InlineTextContext ctx) {
		String unescaped = ctx.getText().chars()
			.collect(() -> new Unescaper(ESCAPE_MAPPING_RAWTEXT), (u, c) -> u.consume((char) c), Unescaper::join)
			.toString();
		return node(new RawText(unescaped));
	}

	@Override
	public TemplateGroupNode visitInlineToken(InlineTokenContext ctx) {
		throw new UnsupportedOperationException("group builder skips visiting inline token node");
	}
	
	@Override
	public TemplateGroupNode visitRefTemplateError(RefTemplateErrorContext ctx) {
		throw new UnsupportedOperationException("group builder skips visiting error nodes");
	}

	public TemplateGroup build() {
		return visit(root).as(TemplateGroup.class);
	}

	private TemplateGroupNode node(Object payload) {
		return new TemplateGroupNode(payload);
	}

	private <T> Optional<T> opt(T object) {
		return Optional.ofNullable(object);
	}

}
