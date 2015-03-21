package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static com.almondtools.comtemplate.engine.expressions.StringLiteral.string;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;

public class MapResolver extends AbstractResolver<ResolvedMapLiteral> {

	private static final String KEYS = "keys";
	private static final String VALUES = "values";
	private static final String ENTRIES = "entries";
	private static final String SIZE = "size";

	public MapResolver() {
		super(ResolvedMapLiteral.class);
	}

	@Override
	public TemplateImmediateExpression resolveTyped(ResolvedMapLiteral base, String function, List<TemplateImmediateExpression> arguments, Scope scope) {
		Map<String, TemplateImmediateExpression> map = base.getMap();
		if (VALUES.equals(function)) {
			List<TemplateImmediateExpression> list = map.values().stream()
				.collect(toList());
			return new ResolvedListLiteral(list);
		} else if (KEYS.equals(function)) {
			List<TemplateImmediateExpression> list = map.keySet().stream()
				.map(key -> string(key))
				.collect(toList());
			return new ResolvedListLiteral(list);
		} else if (ENTRIES.equals(function)) {
			List<TemplateImmediateExpression> list = map.entrySet().stream()
				.map(entry -> new ResolvedMapLiteral(createMapEntry(entry)))
				.collect(toList());
			return new ResolvedListLiteral(list);
		} else if (SIZE.equals(function)) {
			return integer(map.size());
		} else {
			return new ExpressionResolutionError(base, function, arguments, scope, this);
		}
	}

	private Map<String, TemplateImmediateExpression> createMapEntry(Entry<String, TemplateImmediateExpression> entry) {
		Map<String, TemplateImmediateExpression> result = new HashMap<String, TemplateImmediateExpression>();
		result.put(entry.getKey(), entry.getValue());
		return result;
	}

}
