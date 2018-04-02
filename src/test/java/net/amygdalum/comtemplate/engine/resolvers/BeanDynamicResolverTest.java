package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static net.amygdalum.comtemplate.engine.expressions.IntegerLiteral.integer;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.expressions.ExpressionResolutionError;
import net.amygdalum.comtemplate.engine.expressions.NativeObject;
import net.amygdalum.comtemplate.engine.resolvers.BeanDynamicResolver;


public class BeanDynamicResolverTest {

	private BeanDynamicResolver resolver;

	@BeforeEach
	public void before() {
		resolver = new BeanDynamicResolver();
	}

	@Test
	public void testResolveTyped() throws Exception {
		Scope scope = mock(Scope.class);
		NativeObject base = new NativeObject(new JavaBean("f"));
		
		assertThat(resolver.resolveTyped(base, "field", emptyList(), scope), equalTo(new NativeObject("f")));
	}
	
	@Test
	public void testResolveTypedUnresolvable() throws Exception {
		Scope scope = mock(Scope.class);
		NativeObject base = new NativeObject(new JavaBean("f"));
		
		assertThat(resolver.resolveTyped(base, "nofield", emptyList(), scope), instanceOf(ExpressionResolutionError.class));
	}
	
	@Test
	public void testResolveTypedWithArguments() throws Exception {
		Scope scope = mock(Scope.class);
		NativeObject base = new NativeObject(new JavaBean("f"));
		
		assertThat(resolver.resolveTyped(base, "field", asList(integer(1)), scope), instanceOf(ExpressionResolutionError.class));
	}
	
	private static class JavaBean {
		private String field;

		public JavaBean(String field) {
			this.field = field;
		}

		@SuppressWarnings("unused")
		public String getField() {
			return field;
		}
	}

}
