package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.expressions.IntegerLiteral.integer;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.expressions.ExpressionResolutionError;
import com.almondtools.comtemplate.engine.expressions.NativeObject;


public class BeanDynamicResolverTest {

	private BeanDynamicResolver resolver;

	@Before
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
