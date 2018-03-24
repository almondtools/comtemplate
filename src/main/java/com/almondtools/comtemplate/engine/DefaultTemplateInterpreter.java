package com.almondtools.comtemplate.engine;

import static com.almondtools.comtemplate.engine.TemplateVariable.var;
import static com.almondtools.comtemplate.engine.expressions.Evaluated.assembling;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static com.almondtools.comtemplate.engine.expressions.ToObject.SUPERTYPE;
import static com.almondtools.comtemplate.engine.expressions.ToObject.TYPE;
import static com.almondtools.comtemplate.engine.expressions.ToObject.VALUE;
import static com.almondtools.util.stream.ByIndex.byIndex;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import com.almondtools.comtemplate.engine.expressions.BooleanLiteral;
import com.almondtools.comtemplate.engine.expressions.Cast;
import com.almondtools.comtemplate.engine.expressions.Concat;
import com.almondtools.comtemplate.engine.expressions.DecimalLiteral;
import com.almondtools.comtemplate.engine.expressions.Defaulted;
import com.almondtools.comtemplate.engine.expressions.ErrorExpression;
import com.almondtools.comtemplate.engine.expressions.EvalAnonymousTemplate;
import com.almondtools.comtemplate.engine.expressions.EvalAttribute;
import com.almondtools.comtemplate.engine.expressions.EvalContextVar;
import com.almondtools.comtemplate.engine.expressions.EvalFunction;
import com.almondtools.comtemplate.engine.expressions.EvalTemplate;
import com.almondtools.comtemplate.engine.expressions.EvalTemplateFunction;
import com.almondtools.comtemplate.engine.expressions.EvalTemplateMixed;
import com.almondtools.comtemplate.engine.expressions.EvalVar;
import com.almondtools.comtemplate.engine.expressions.EvalVirtual;
import com.almondtools.comtemplate.engine.expressions.Evaluated;
import com.almondtools.comtemplate.engine.expressions.Exists;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;
import com.almondtools.comtemplate.engine.expressions.IntegerLiteral;
import com.almondtools.comtemplate.engine.expressions.ListLiteral;
import com.almondtools.comtemplate.engine.expressions.MapLiteral;
import com.almondtools.comtemplate.engine.expressions.NativeObject;
import com.almondtools.comtemplate.engine.expressions.RawText;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;
import com.almondtools.comtemplate.engine.expressions.StringLiteral;
import com.almondtools.comtemplate.engine.expressions.TemplateResolutionError;
import com.almondtools.comtemplate.engine.expressions.ToObject;
import com.almondtools.comtemplate.engine.expressions.UnexpectedTypeError;
import com.almondtools.comtemplate.engine.expressions.VariableResolutionError;

public class DefaultTemplateInterpreter implements TemplateInterpreter {

	private ResolverRegistry resolvers;
	private GlobalTemplates templates;
	private ErrorHandler handler;

	public DefaultTemplateInterpreter() {
		this(ResolverRegistry.defaultRegistry(), GlobalTemplates.defaultTemplates(), new DefaultErrorHandler());
	}

	public DefaultTemplateInterpreter(ResolverRegistry resolvers, GlobalTemplates templates, ErrorHandler handler) {
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
		return scope.resolveVariable(name, definition)
			.map(v -> v.getValue().apply(this, scope))
			.orElseGet(() -> new VariableResolutionError(name, definition));
	}

	@Override
	public TemplateImmediateExpression visitEvalContextVar(EvalContextVar evalContextVar, Scope scope) {
		String name = evalContextVar.getName();
		Optional<TemplateVariable> value = scope.resolveContextVariable(name);
		if (!value.isPresent()) {
			value = templates.resolveGlobal(name);
		}
		return value.map(v -> v.getValue().apply(this, scope))
			.orElseGet(() -> new VariableResolutionError(name, scope));
	}

	@Override
	public TemplateImmediateExpression visitEvalTemplate(EvalTemplate evalTemplate, Scope scope) {
		String name = evalTemplate.getTemplate();
		TemplateDefinition definition = evalTemplate.getDefinition();
		List<TemplateVariable> arguments = evalTemplate.getArguments();
		TemplateDefinition template = templates.resolve(name);
		if (template == null) {
			template = scope.resolveTemplate(name, definition);
		}
		if (template == null) {
			return new TemplateResolutionError(name, definition);
		}
		return template.evaluate(this, scope, arguments);
	}

	@Override
	public TemplateImmediateExpression visitEvalTemplateFunction(EvalTemplateFunction evalTemplateFunction, Scope scope) {
		String name = evalTemplateFunction.getTemplate();
		TemplateDefinition definition = evalTemplateFunction.getDefinition();
		List<TemplateExpression> arguments = evalTemplateFunction.getArguments();
		TemplateDefinition template = templates.resolve(name);
		if (template == null) {
			template = scope.resolveTemplate(name, definition);
		}
		if (template == null) {
			return new TemplateResolutionError(name, definition);
		}
		List<TemplateVariable> assignedArguments = template.getParameters().stream()
			.limit(arguments.size())
			.map(byIndex(arguments.size()))
			.map(item -> var(item.value.getName(), arguments.get(item.index)))
			.collect(toList());
		return template.evaluate(this, scope, assignedArguments);
	}

	@Override
	public TemplateImmediateExpression visitEvalTemplateMixed(EvalTemplateMixed evalTemplateMixed, Scope scope) {
		String name = evalTemplateMixed.getTemplate();
		TemplateDefinition definition = evalTemplateMixed.getDefinition();
		List<TemplateExpression> arguments = evalTemplateMixed.getArguments();
		List<TemplateVariable> namedArguments = evalTemplateMixed.getNamedArguments();
		TemplateDefinition template = templates.resolve(name);
		if (template == null) {
			template = scope.resolveTemplate(name, definition);
		}
		if (template == null) {
			return new TemplateResolutionError(name, definition);
		}
		List<TemplateVariable> assignedArguments = Stream.concat(
			template.getParameters().stream()
				.limit(arguments.size())
				.map(byIndex(arguments.size()))
				.map(item -> var(item.value.getName(), arguments.get(item.index))),
			namedArguments.stream())
			.collect(toList());
		return template.evaluate(this, scope, assignedArguments);
	}

	@Override
	public TemplateImmediateExpression visitEvalAnonymousTemplate(EvalAnonymousTemplate evalAnonymousTemplate, Scope scope) {
		Scope next = new Scope(scope, scope.getDefinition(), scope.getVariables());
		return evalAnonymousTemplate.getExpressions().stream()
			.map(expression -> expression.apply(this, next))
			.collect(assembling());
	}

	@Override
	public TemplateImmediateExpression visitEvalAttribute(EvalAttribute evalAttribute, Scope scope) {
		TemplateExpression base = evalAttribute.getBase();
		String attribute = evalAttribute.getAttribute();
		TemplateImmediateExpression resolved = base.apply(this, scope);
		Resolver resolver = resolvers.getResolverFor(resolved);
		return resolver.resolve(resolved, attribute, emptyList(), scope);
	}

	@Override
	public TemplateImmediateExpression visitEvalVirtual(EvalVirtual evalVirtual, Scope scope) {
		TemplateExpression base = evalVirtual.getBase();
		TemplateImmediateExpression resolved = base.apply(this, scope);
		TemplateImmediateExpression attribute = evalVirtual.getAttribute().apply(this, scope);
		Resolver resolver = resolvers.getResolverFor(resolved);
		return resolver.resolve(resolved, attribute.getText(), emptyList(), scope);
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
		return resolver.resolve(resolved, function, arguments, scope);
	}

	@Override
	public TemplateImmediateExpression visitEvaluated(Evaluated evaluated, Scope scope) {
		return evaluated;
	}

	@Override
	public TemplateImmediateExpression visitExists(Exists exists, Scope scope) {
		TemplateImmediateExpression result = exists.getExpression().apply(this, scope);
		if (result instanceof VariableResolutionError || result instanceof ExpressionResolutionError) {
			return BooleanLiteral.FALSE;
		} else {
			return BooleanLiteral.TRUE;
		}
	}

	@Override
	public TemplateImmediateExpression visitDefaulted(Defaulted defaulted, Scope scope) {
		TemplateExpression result = defaulted.getExpression().apply(this, scope);
		if (result instanceof VariableResolutionError || result instanceof ExpressionResolutionError) {
			return defaulted.getDefaultExpression().apply(this, scope);
		} else {
			return result.apply(this, scope);
		}
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
			return new UnexpectedTypeError(type, evaluated);
		} else {
			ResolvedMapLiteral object = (ResolvedMapLiteral) evaluated;
			if (superTypes(object)
				.map(expression -> expression.getText())
				.filter(name -> type.equals(name))
				.findAny().isPresent()) {
				return object;
			} else {
				return new UnexpectedTypeError(type, evaluated);
			}
		}
	}

	@Override
	public TemplateImmediateExpression visitErrorExpression(ErrorExpression errorExpression, Scope scope) {
		return handler.handle(errorExpression);
	}

}
