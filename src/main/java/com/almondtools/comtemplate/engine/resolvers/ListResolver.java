package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.ErrorExpression;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;
import com.almondtools.comtemplate.engine.expressions.ResolvedListLiteral;

public class ListResolver extends ExclusiveTypeAggregateResolver<ResolvedListLiteral> {

	public ListResolver() {
		super(ResolvedListLiteral.class,
				new Size(), 
				new First(), 
				new Last(), 
				new Rest(), 
				new Trunc(), 
				new StripErrors());
	}

	public static class Size extends ExclusiveTypeFunctionResolver<ResolvedListLiteral> {

		public Size() {
			super(ResolvedListLiteral.class, "size");
		}

		@Override
		public TemplateImmediateExpression resolveTyped(ResolvedListLiteral base, List<TemplateImmediateExpression> arguments, Scope scope) {
			List<TemplateImmediateExpression> list = base.getList();
			int size = list.size();
			return integer(size);
		}

	}

	public static abstract class Extract extends ExclusiveTypeFunctionResolver<ResolvedListLiteral> {

		public Extract(String name) {
			super(ResolvedListLiteral.class, name);
		}

		@Override
		public TemplateImmediateExpression resolveTyped(ResolvedListLiteral base, List<TemplateImmediateExpression> arguments, Scope scope) {
			List<TemplateImmediateExpression> list = base.getList();
			int size = list.size();
			int index = getIndex(size);
			if (size <= index || index < 0) {
				return new ExpressionResolutionError(base, getName(), arguments, scope, this);
			} else {
				return list.get(index);
			}
		}

		public abstract int getIndex(int size);
	}

	public static class First extends Extract {

		public First() {
			super("first");
		}

		@Override
		public int getIndex(int size) {
			return 0;
		}

	}

	public static class Last extends Extract {

		public Last() {
			super("last");
		}

		@Override
		public int getIndex(int size) {
			return size - 1;
		}

	}

	public static abstract class Chop extends ExclusiveTypeFunctionResolver<ResolvedListLiteral> {

		public Chop(String name) {
			super(ResolvedListLiteral.class, name);
		}

		@Override
		public TemplateImmediateExpression resolveTyped(ResolvedListLiteral base, List<TemplateImmediateExpression> arguments, Scope scope) {
			List<TemplateImmediateExpression> list = base.getList();
			int size = list.size();
			int index = getIndex(size);
			if (size <= index || index < 0) {
				return new ExpressionResolutionError(base, getName(), arguments, scope, this);
			} else {
				List<TemplateImmediateExpression> stripped = new ArrayList<TemplateImmediateExpression>(list);
				stripped.remove(index);
				return new ResolvedListLiteral(stripped);
			}
		}

		public abstract int getIndex(int size);
	}

	public static class Rest extends Chop {

		public Rest() {
			super("rest");
		}

		@Override
		public int getIndex(int size) {
			return 0;
		}

	}

	public static class Trunc extends Chop {

		public Trunc() {
			super("trunc");
		}

		@Override
		public int getIndex(int size) {
			return size - 1;
		}

	}

	public static class StripErrors extends ExclusiveTypeFunctionResolver<ResolvedListLiteral> {

		public StripErrors() {
			super(ResolvedListLiteral.class, "strip");
		}

		@Override
		public TemplateImmediateExpression resolveTyped(ResolvedListLiteral base, List<TemplateImmediateExpression> arguments, Scope scope) {
			List<TemplateImmediateExpression> list = base.getList();
			int size = list.size();
			if (size == 0) {
				return base;
			} else {
				List<TemplateImmediateExpression> stripped = list.stream().filter(item -> !(item instanceof ErrorExpression)).collect(toList());
				return new ResolvedListLiteral(stripped);
			}
		}

	}

}
