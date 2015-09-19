package com.almondtools.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateExpressionVisitor;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;

public class Evaluated implements TemplateImmediateExpression {

	private List<TemplateImmediateExpression> evaluated;

	public Evaluated(List<TemplateImmediateExpression> evaluated) {
		this.evaluated = evaluated;
	}

	public Evaluated(TemplateImmediateExpression... evaluated) {
		this(asList(evaluated));
	}

	public List<TemplateImmediateExpression> getEvaluated() {
		return evaluated;
	}

	@Override
	public String getText() {
		return evaluated.stream()
			.filter(Objects::nonNull)
			.map(TemplateImmediateExpression::getText)
			.collect(joining());
	}

	@Override
	public <T> T as(Class<T> clazz) {
		return null;
	}

	@Override
	public <T> T apply(TemplateExpressionVisitor<T> visitor, Scope scope) {
		return visitor.visitEvaluated(this, scope);
	}

	@Override
	public String toString() {
		return evaluated.stream()
			.map(expression -> expression.toString())
			.collect(joining(",", "{", "}"));
	}

	public static Collector<TemplateImmediateExpression, List<TemplateImmediateExpression>, Evaluated> assembling() {
		return new Collector<TemplateImmediateExpression, List<TemplateImmediateExpression>, Evaluated>() {

			@Override
			public Supplier<List<TemplateImmediateExpression>> supplier() {
				return ArrayList::new;
			}

			@Override
			public BiConsumer<List<TemplateImmediateExpression>, TemplateImmediateExpression> accumulator() {
				return (list, element) -> list.add(element);
			}

			@Override
			public BinaryOperator<List<TemplateImmediateExpression>> combiner() {
				return (list1, list2) -> {
					List<TemplateImmediateExpression> list = new ArrayList<TemplateImmediateExpression>(list1.size() + list2.size());
					list.addAll(list1);
					list.addAll(list2);
					return list;
				};
			}

			@Override
			public Function<List<TemplateImmediateExpression>, Evaluated> finisher() {
				return Evaluated::new;
			}

			@Override
			public Set<Collector.Characteristics> characteristics() {
				return EnumSet.noneOf(Collector.Characteristics.class);
			}

		};
	}

}
