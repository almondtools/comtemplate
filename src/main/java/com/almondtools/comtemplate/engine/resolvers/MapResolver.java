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
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;
import com.almondtools.comtemplate.engine.expressions.ResolvedMapLiteral;

public class MapResolver extends ExclusiveTypeAggregateResolver<ResolvedMapLiteral> {

	public MapResolver() {
		super(ResolvedMapLiteral.class,
				new Values(), 
				new Keys(), 
				new Entries(), 
				new Size());
	}	

	public static class Values extends ExclusiveTypeFunctionResolver<ResolvedMapLiteral> {

		public Values() {
			super(ResolvedMapLiteral.class, "values");
		}

		@Override
		public TemplateImmediateExpression resolveTyped(ResolvedMapLiteral base, List<TemplateImmediateExpression> arguments, Scope scope) {
			List<TemplateImmediateExpression> list = base.getMap().values().stream().collect(toList());
			return new ResolvedListLiteral(list);
		}

	}

	public static class Keys extends ExclusiveTypeFunctionResolver<ResolvedMapLiteral> {

		public Keys() {
			super(ResolvedMapLiteral.class, "keys");
		}

		@Override
		public TemplateImmediateExpression resolveTyped(ResolvedMapLiteral base, List<TemplateImmediateExpression> arguments, Scope scope) {
			List<TemplateImmediateExpression> list = base.getMap().keySet().stream().map(key -> string(key)).collect(toList());
			return new ResolvedListLiteral(list);
		}

	}

	public static class Entries extends ExclusiveTypeFunctionResolver<ResolvedMapLiteral> {

		public Entries() {
			super(ResolvedMapLiteral.class, "entries");
		}

		@Override
		public TemplateImmediateExpression resolveTyped(ResolvedMapLiteral base, List<TemplateImmediateExpression> arguments, Scope scope) {
			List<TemplateImmediateExpression> list = base.getMap().entrySet().stream().map(entry -> new ResolvedMapLiteral(createMapEntry(entry))).collect(toList());
			return new ResolvedListLiteral(list);
		}

		private Map<String, TemplateImmediateExpression> createMapEntry(Entry<String, TemplateImmediateExpression> entry) {
			Map<String, TemplateImmediateExpression> result = new HashMap<String, TemplateImmediateExpression>();
			result.put(entry.getKey(), entry.getValue());
			return result;
		}

	}

	public static class Size extends ExclusiveTypeFunctionResolver<ResolvedMapLiteral> {

		public Size() {
			super(ResolvedMapLiteral.class, "size");
		}

		@Override
		public TemplateImmediateExpression resolveTyped(ResolvedMapLiteral base, List<TemplateImmediateExpression> arguments, Scope scope) {
			return integer(base.getMap().size());
		}

	}

}
