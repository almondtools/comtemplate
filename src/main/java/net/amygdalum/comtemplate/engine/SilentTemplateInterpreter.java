package net.amygdalum.comtemplate.engine;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.Evaluated.assembling;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static net.amygdalum.comtemplate.engine.expressions.ToObject.SUPERTYPE;
import static net.amygdalum.comtemplate.engine.expressions.ToObject.TYPE;
import static net.amygdalum.comtemplate.engine.expressions.ToObject.VALUE;
import static net.amygdalum.util.stream.ByIndex.byIndex;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import net.amygdalum.comtemplate.engine.expressions.BooleanLiteral;
import net.amygdalum.comtemplate.engine.expressions.Cast;
import net.amygdalum.comtemplate.engine.expressions.Concat;
import net.amygdalum.comtemplate.engine.expressions.DecimalLiteral;
import net.amygdalum.comtemplate.engine.expressions.Defaulted;
import net.amygdalum.comtemplate.engine.expressions.ErrorExpression;
import net.amygdalum.comtemplate.engine.expressions.EvalAnonymousTemplate;
import net.amygdalum.comtemplate.engine.expressions.EvalAttribute;
import net.amygdalum.comtemplate.engine.expressions.EvalContextVar;
import net.amygdalum.comtemplate.engine.expressions.EvalFunction;
import net.amygdalum.comtemplate.engine.expressions.EvalTemplate;
import net.amygdalum.comtemplate.engine.expressions.EvalTemplateFunction;
import net.amygdalum.comtemplate.engine.expressions.EvalTemplateMixed;
import net.amygdalum.comtemplate.engine.expressions.EvalVar;
import net.amygdalum.comtemplate.engine.expressions.EvalVirtual;
import net.amygdalum.comtemplate.engine.expressions.Evaluated;
import net.amygdalum.comtemplate.engine.expressions.Exists;
import net.amygdalum.comtemplate.engine.expressions.IgnoreErrors;
import net.amygdalum.comtemplate.engine.expressions.IntegerLiteral;
import net.amygdalum.comtemplate.engine.expressions.ListLiteral;
import net.amygdalum.comtemplate.engine.expressions.MapLiteral;
import net.amygdalum.comtemplate.engine.expressions.NativeObject;
import net.amygdalum.comtemplate.engine.expressions.RawText;
import net.amygdalum.comtemplate.engine.expressions.ResolvedListLiteral;
import net.amygdalum.comtemplate.engine.expressions.ResolvedMapLiteral;
import net.amygdalum.comtemplate.engine.expressions.StringLiteral;
import net.amygdalum.comtemplate.engine.expressions.TemplateResolutionError;
import net.amygdalum.comtemplate.engine.expressions.ToObject;
import net.amygdalum.comtemplate.engine.expressions.UnexpectedTypeError;
import net.amygdalum.comtemplate.engine.expressions.VariableResolutionError;

public class SilentTemplateInterpreter implements TemplateInterpreter {

	private TemplateLoader loader;
	private ResolverRegistry resolvers;
	private GlobalTemplates templates;
	private ErrorHandler handler;

	public SilentTemplateInterpreter(TemplateLoader loader, ResolverRegistry resolvers, GlobalTemplates templates, ErrorHandler handler) {
		this.loader = loader;
		this.resolvers = resolvers;
		this.templates = templates;
		this.handler = handler;
	}

	@Override
	public TemplateImmediateExpression visitRawText(RawText rawText, Scope scope) {
		return rawText;
	}

	@Override
	public TemplateImmediateExpression visitEvalVar(EvalVar evalVar, Scope scope) {
		String name = evalVar.getName();
		TemplateDefinition definition = evalVar.getDefinition();
		return handler.clear(evalVar, scope.resolveVariable(name, definition)
			.map(v -> v.getValue().apply(this, scope))
			.orElseGet(() -> new VariableResolutionError(name, definition)));
	}

	@Override
	public TemplateImmediateExpression visitEvalContextVar(EvalContextVar evalContextVar, Scope scope) {
		String name = evalContextVar.getName();
		Optional<TemplateVariable> value = scope.resolveContextVariable(name);
		if (!value.isPresent()) {
			value = templates.resolveGlobal(name);
		}
		return handler.clear(evalContextVar, value.map(v -> v.getValue().apply(this, scope))
			.orElseGet(() -> new VariableResolutionError(name, scope)));
	}

	@Override
	public TemplateImmediateExpression visitEvalTemplate(EvalTemplate evalTemplate, Scope scope) {
		String name = evalTemplate.getTemplate();
		TemplateDefinition definition = evalTemplate.getDefinition();
		List<TemplateVariable> arguments = evalTemplate.getArguments();
		TemplateDefinition template = resolveTemplate(definition, scope, name);
		if (template == null) {
			return handler.clear(evalTemplate, new TemplateResolutionError(name, definition));
		}
		return handler.clear(evalTemplate, template.evaluate(this, scope, arguments));
	}

	@Override
	public TemplateImmediateExpression visitEvalTemplateFunction(EvalTemplateFunction evalTemplateFunction, Scope scope) {
		String name = evalTemplateFunction.getTemplate();
		TemplateDefinition definition = evalTemplateFunction.getDefinition();
		List<TemplateExpression> arguments = evalTemplateFunction.getArguments();
		TemplateDefinition template = resolveTemplate(definition, scope, name);
		if (template == null) {
			return handler.clear(evalTemplateFunction, new TemplateResolutionError(name, definition));
		}
		List<TemplateVariable> assignedArguments = template.getParameters().stream()
			.limit(arguments.size())
			.map(byIndex(arguments.size()))
			.map(item -> var(item.value.getName(), arguments.get(item.index)))
			.collect(toList());
		return handler.clear(evalTemplateFunction, template.evaluate(this, scope, assignedArguments));
	}

	@Override
	public TemplateImmediateExpression visitEvalTemplateMixed(EvalTemplateMixed evalTemplateMixed, Scope scope) {
		String name = evalTemplateMixed.getTemplate();
		TemplateDefinition definition = evalTemplateMixed.getDefinition();
		List<TemplateExpression> arguments = evalTemplateMixed.getArguments();
		List<TemplateVariable> namedArguments = evalTemplateMixed.getNamedArguments();
		TemplateDefinition template = resolveTemplate(definition, scope, name);
		if (template == null) {
			return handler.clear(evalTemplateMixed, new TemplateResolutionError(name, definition));
		}
		List<TemplateVariable> assignedArguments = Stream.concat(
			template.getParameters().stream()
				.limit(arguments.size())
				.map(byIndex(arguments.size()))
				.map(item -> var(item.value.getName(), arguments.get(item.index))),
			namedArguments.stream())
			.collect(toList());
		return handler.clear(evalTemplateMixed, template.evaluate(this, scope, assignedArguments));
	}

	public TemplateDefinition resolveTemplate(TemplateDefinition definition, Scope scope, String name) {
		TemplateDefinition template = templates.resolve(name);
		if (template == null) {
			template = scope.resolveTemplate(name, definition);
		}
		if (template == null && isAbsoluteReference(name)) {
			template = loader.loadDefinition(name);
		}
		return template;
	}

	private boolean isAbsoluteReference(String name) {
		return name.indexOf('.') > -1;
	}

	@Override
	public TemplateImmediateExpression visitEvalAnonymousTemplate(EvalAnonymousTemplate evalAnonymousTemplate, Scope scope) {
		Scope next = new Scope(scope, new AnonymousTemplateDefinition(scope.getGroup()));
		return handler.clear(evalAnonymousTemplate, evalAnonymousTemplate.getExpressions().stream()
			.map(expression -> expression.apply(this, next))
			.collect(assembling()));
	}

	@Override
	public TemplateImmediateExpression visitEvalAttribute(EvalAttribute evalAttribute, Scope scope) {
		TemplateExpression base = evalAttribute.getBase();
		String attribute = evalAttribute.getAttribute();
		TemplateImmediateExpression resolved = base.apply(this, scope);
		Resolver resolver = resolvers.getResolverFor(resolved);
		return handler.clear(evalAttribute, resolver.resolve(resolved, attribute, emptyList(), scope));
	}

	@Override
	public TemplateImmediateExpression visitEvalVirtual(EvalVirtual evalVirtual, Scope scope) {
		TemplateExpression base = evalVirtual.getBase();
		TemplateImmediateExpression resolved = base.apply(this, scope);
		TemplateImmediateExpression attribute = evalVirtual.getAttribute().apply(this, scope);
		Resolver resolver = resolvers.getResolverFor(resolved);
		return handler.clear(evalVirtual, resolver.resolve(resolved, attribute.getText(), emptyList(), scope));
	}

	@Override
	public TemplateImmediateExpression visitEvalFunction(EvalFunction evalFunction, Scope scope) {
		TemplateExpression base = evalFunction.getBase();
		String function = evalFunction.getFunction();
		List<TemplateImmediateExpression> arguments = evalFunction.getArguments().stream()
			.map(expression -> expression.apply(this, scope))
			.collect(toList());
		TemplateImmediateExpression resolved = base.apply(this, scope);
		Resolver resolver = resolvers.getResolverFor(resolved);
		return handler.clear(evalFunction, resolver.resolve(resolved, function, arguments, scope));
	}

	@Override
	public TemplateImmediateExpression visitEvaluated(Evaluated evaluated, Scope scope) {
		return evaluated;
	}

	@Override
	public TemplateImmediateExpression visitIgnoreErrors(IgnoreErrors ignoreErrors, Scope scope) {
		handler.addDefault(ignoreErrors.getExpression(), () -> BooleanLiteral.FALSE);
		TemplateImmediateExpression result = ignoreErrors.getExpression().apply(this, scope);
		return result;
	}
	
	@Override
	public TemplateImmediateExpression visitExists(Exists exists, Scope scope) {
		handler.addDefault(exists.getExpression(), () -> BooleanLiteral.FALSE);
		TemplateImmediateExpression result = exists.getExpression().apply(this, scope);
		if (result != BooleanLiteral.FALSE) {
			result = BooleanLiteral.TRUE;
		}
		return result;
	}

	@Override
	public TemplateImmediateExpression visitDefaulted(Defaulted defaulted, Scope scope) {
		handler.addDefault(defaulted.getExpression(), () -> defaulted.getDefaultExpression().apply(this, scope));
		return defaulted.getExpression().apply(this, scope);
	}

	@Override
	public TemplateImmediateExpression visitConcat(Concat concat, Scope scope) {
		List<TemplateImmediateExpression> evaluated = concat.getExpressions().stream()
			.map(expression -> expression.apply(this, scope))
			.collect(toList());
		boolean isMap = evaluated.stream()
			.allMatch(expression -> expression instanceof ResolvedMapLiteral);
		boolean isList = evaluated.stream()
			.anyMatch(expression -> expression instanceof ResolvedListLiteral);
		List<TemplateImmediateExpression> allSuperTypes = evaluated.stream()
			.filter(expression -> expression instanceof ResolvedMapLiteral)
			.map(expression -> (ResolvedMapLiteral) expression)
			.flatMap(expression -> superTypes(expression))
			.collect(toList());
		if (isMap) {
			Map<String, TemplateImmediateExpression> map = evaluated.stream()
				.filter(expression -> expression instanceof ResolvedMapLiteral)
				.map(expression -> (ResolvedMapLiteral) expression)
				.flatMap(mapElement -> mapElement.getMap().entrySet().stream())
				.collect(toMap(e -> e.getKey(), e -> e.getValue(), (o, n) -> n, LinkedHashMap::new));
			if (allSuperTypes.size() > 1) {
				map.remove(TYPE);
				map.put(SUPERTYPE, new ResolvedListLiteral(allSuperTypes));
			}
			return new ResolvedMapLiteral(map);
		} else if (isList) {
			List<TemplateImmediateExpression> list = evaluated.stream()
				.flatMap(element -> {
					if (element instanceof ResolvedListLiteral) {
						return ((ResolvedListLiteral) element).getList().stream();
					} else {
						return Stream.of(element);
					}
				})
				.collect(toList());
			return new ResolvedListLiteral(list);
		} else {
			return new Evaluated(evaluated);
		}
	}

	protected Stream<TemplateImmediateExpression> superTypes(ResolvedMapLiteral expression) {
		Builder<TemplateImmediateExpression> buffer = Stream.<TemplateImmediateExpression> builder();
		TemplateImmediateExpression type = expression.getAttribute(TYPE);
		if (type != null) {
			buffer.add(type);
		}
		TemplateImmediateExpression superTypes = expression.getAttribute(SUPERTYPE);
		if (superTypes instanceof ResolvedListLiteral) {
			for (TemplateImmediateExpression superType : ((ResolvedListLiteral) superTypes).getList()) {
				buffer.add(superType);
			}
		} else if (superTypes != null) {
			buffer.add(superTypes);
		}
		return buffer.build();
	}

	@Override
	public TemplateImmediateExpression visitToObject(ToObject toObject, Scope scope) {
		TemplateImmediateExpression resolved = toObject.getExpression().apply(this, scope);
		Map<String, TemplateImmediateExpression> map = new LinkedHashMap<String, TemplateImmediateExpression>();
		map.put(TYPE, string(toObject.getType()));
		if (resolved instanceof ResolvedMapLiteral) {
			Map<String, TemplateImmediateExpression> attributes = new LinkedHashMap<>(((ResolvedMapLiteral) resolved).getMap());
			attributes.remove(TYPE);
			attributes.remove(SUPERTYPE);
			List<TemplateImmediateExpression> superTypes = superTypes(((ResolvedMapLiteral) resolved)).collect(toList());
			if (!superTypes.isEmpty()) {
				map.put(SUPERTYPE, new ResolvedListLiteral(superTypes));
			}
			map.putAll(attributes);
		} else {
			map.put(VALUE, resolved);
		}
		return new ResolvedMapLiteral(map);
	}

	@Override
	public TemplateImmediateExpression visitMapLiteral(MapLiteral mapLiteral, Scope scope) {
		List<TemplateVariable> entries = mapLiteral.getMap().entrySet().stream()
			.map(entry -> var(entry.getKey(), entry.getValue().apply(this, scope)))
			.collect(toList());
		return new ResolvedMapLiteral(entries);
	}

	@Override
	public TemplateImmediateExpression visitMapLiteral(ResolvedMapLiteral mapLiteral, Scope scope) {
		return mapLiteral;
	}

	@Override
	public TemplateImmediateExpression visitListLiteral(ListLiteral listLiteral, Scope scope) {
		List<TemplateImmediateExpression> list = listLiteral.getList().stream()
			.map(item -> item.apply(this, scope))
			.collect(toList());
		return new ResolvedListLiteral(list);
	}

	@Override
	public TemplateImmediateExpression visitListLiteral(ResolvedListLiteral listLiteral, Scope scope) {
		return listLiteral;
	}

	@Override
	public TemplateImmediateExpression visitStringLiteral(StringLiteral stringLiteral, Scope scope) {
		return stringLiteral;
	}

	@Override
	public TemplateImmediateExpression visitIntegerLiteral(IntegerLiteral integerLiteral, Scope scope) {
		return integerLiteral;
	}

	@Override
	public TemplateImmediateExpression visitDecimalLiteral(DecimalLiteral decimalLiteral, Scope scope) {
		return decimalLiteral;
	}

	@Override
	public TemplateImmediateExpression visitBooleanLiteral(BooleanLiteral booleanLiteral, Scope scope) {
		return booleanLiteral;
	}

	@Override
	public TemplateImmediateExpression visitNativeObject(NativeObject nativeObject, Scope scope) {
		return nativeObject;
	}

	@Override
	public TemplateImmediateExpression visitCast(Cast cast, Scope scope) {
		String type = cast.getType();
		TemplateImmediateExpression evaluated = cast.getExpression().apply(this, scope);
		if (!(evaluated instanceof ResolvedMapLiteral)) {
			return handler.clear(cast, new UnexpectedTypeError(type, evaluated));
		} else {
			ResolvedMapLiteral object = (ResolvedMapLiteral) evaluated;
			if (superTypes(object)
				.map(expression -> expression.getText())
				.filter(name -> type.equals(name))
				.findAny().isPresent()) {
				return object;
			} else {
				return handler.clear(cast, new UnexpectedTypeError(type, evaluated));
			}
		}
	}

	@Override
	public TemplateImmediateExpression visitErrorExpression(ErrorExpression errorExpression, Scope scope) {
		return handler.clear(errorExpression, errorExpression);
	}

}
