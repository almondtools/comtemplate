package net.amygdalum.comtemplate.engine.resolvers;

import java.util.List;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.ResolvedMapLiteral;
import net.amygdalum.comtemplate.engine.expressions.StringLiteral;

public class TestAdaptor extends MonomophousAdaptor {
	public TestAdaptor(String type) {
		super(type);
	}

	@Override
	public TemplateImmediateExpression resolve(ResolvedMapLiteral base, List<TemplateImmediateExpression> arguments, Scope scope) {
		String type = getType();
		if (type == "exception") {
			throw new RuntimeException();
		}
		return StringLiteral.string(type);
	}
}