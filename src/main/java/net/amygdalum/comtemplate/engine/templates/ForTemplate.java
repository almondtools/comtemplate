package net.amygdalum.comtemplate.engine.templates;

import static java.util.Arrays.asList;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.Evaluated.assembling;
import static net.amygdalum.comtemplate.engine.expressions.IntegerLiteral.integer;
import static net.amygdalum.util.stream.ByIndex.byIndex;

import java.util.List;

import net.amygdalum.comtemplate.engine.ArgumentRequiredException;
import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateDefinition;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.TemplateInterpreter;
import net.amygdalum.comtemplate.engine.TemplateVariable;
import net.amygdalum.comtemplate.engine.expressions.ResolvedListLiteral;
import net.amygdalum.comtemplate.engine.expressions.UnexpectedTypeError;

public class ForTemplate extends TemplateDefinition {

	public static final String NAME = "for";
	public static final String ITEM = "item";
	public static final String I = "i";
	public static final String I0 = "i0";
	public static final String DO = "do";

	public ForTemplate() {
		super(NAME, ITEM, DO);
	}

	@Override
	public TemplateImmediateExpression evaluate(TemplateInterpreter interpreter, Scope parent, List<TemplateVariable> arguments) {
		TemplateVariable in = findVariable(DO, arguments)
			.orElseThrow(() -> new ArgumentRequiredException(DO));
		TemplateVariable var = arguments.stream()
			.filter(argument -> !argument.getName().equals(DO))
			.findFirst()
			.orElseThrow(() -> new ArgumentRequiredException(ITEM));
		String name = var.getName();
		TemplateImmediateExpression evaluated = var.getValue().apply(interpreter, parent);
		Scope scope = new Scope(parent, this, arguments);
		if (evaluated instanceof ResolvedListLiteral) {
			List<TemplateImmediateExpression> list = ((ResolvedListLiteral) evaluated).getList();
			return list.stream()
				.map(byIndex(list.size()))
				.map(item -> {
					TemplateVariable ivar = var(name, item.value);
					TemplateVariable i = var(I, integer(item.index + 1));
					TemplateVariable i0 = var(I0, integer(item.index));
					Scope iscope = new Scope(scope, this, asList(i, i0, ivar));
					return in.getValue().apply(interpreter, iscope);
				})
				.collect(assembling());
		} else {
			return new UnexpectedTypeError("list", evaluated);
		}
	}

}
