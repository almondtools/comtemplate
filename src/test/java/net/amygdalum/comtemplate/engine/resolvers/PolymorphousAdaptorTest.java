package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Collections.emptyList;
import static net.amygdalum.comtemplate.engine.TemplateVariable.var;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateImmediateExpression;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.expressions.ResolvedMapLiteral;

public class PolymorphousAdaptorTest {

	@SuppressWarnings({ "unchecked" })
	@Test
	public void testGetResolvedClasses() throws Exception {
		TestPolymorphousAdaptor adaptor = new TestPolymorphousAdaptor("poly");

		assertThat(adaptor.getResolvedClasses(), contains(ResolvedMapLiteral.class));
	}

	@Test
	public void testResolve() throws Exception {
		TestPolymorphousAdaptor adaptor = new TestPolymorphousAdaptor("poly", "type1", "type2");

		TemplateImmediateExpression resolved = adaptor.resolve(new ResolvedMapLiteral(var("_type", string("type1"))), "poly", emptyList(), aScope());

		assertThat(resolved, equalTo(string("type1")));
	}

	@Test
	public void testResolveOtherType() throws Exception {
		TestPolymorphousAdaptor adaptor = new TestPolymorphousAdaptor("poly", "type1", "type2");

		TemplateImmediateExpression resolved = adaptor.resolve(new ResolvedMapLiteral(var("_type", string("type2"))), "poly", emptyList(), aScope());

		assertThat(resolved, equalTo(string("type2")));
	}

	@Test
	public void testResolveUnsupportedType() throws Exception {
		TestPolymorphousAdaptor adaptor = new TestPolymorphousAdaptor("poly", "type1", "type2");

		TemplateImmediateExpression resolved = adaptor.resolve(new ResolvedMapLiteral(var("_type", string("type3"))), "poly", emptyList(), aScope());

		assertThat(resolved, instanceOf(ExpressionResolutionError.class));
	}

	@Test
	public void testResolveException() throws Exception {
		TestPolymorphousAdaptor adaptor = new TestPolymorphousAdaptor("poly", "type1", "type2", "exception");

		TemplateImmediateExpression resolved = adaptor.resolve(new ResolvedMapLiteral(var("_type", string("exception"))), "poly", emptyList(), aScope());

		assertThat(resolved, instanceOf(ExpressionResolutionError.class));
	}

	@Test
	public void testResolveRejectNonMap() throws Exception {
		TestPolymorphousAdaptor adaptor = new TestPolymorphousAdaptor("poly", "type1", "type2");
		
		TemplateImmediateExpression resolved = adaptor.resolve(string("reject"), "poly", emptyList(), aScope());
		
		assertThat(resolved, instanceOf(ExpressionResolutionError.class));
	}
	
	@Test
	public void testResolveRejectFunction() throws Exception {
		TestPolymorphousAdaptor adaptor = new TestPolymorphousAdaptor("poly", "type1", "type2");
		
		TemplateImmediateExpression resolved = adaptor.resolve(new ResolvedMapLiteral(var("_type", string("type1"))), "nonpoly", emptyList(), aScope());
		
		assertThat(resolved, instanceOf(ExpressionResolutionError.class));
	}
	
	private Scope aScope() {
		return Mockito.mock(Scope.class);
	}

}
