package net.amygdalum.comtemplate.engine.expressions;

import static com.almondtools.conmatch.conventions.EqualityMatcher.satisfiesDefaultEquality;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;


public class StringLiteralTest {

	@Test
	public void testGetValue() throws Exception {
		assertThat(string("string").getValue(), equalTo("string"));
	}

	@Test
	public void testGetText() throws Exception {
		assertThat(string("string").getText(), equalTo("string"));
	}

	@Test
	public void testAsString() throws Exception {
		assertThat(string("text").as(String.class), equalTo("text"));
	}

	@Test
	public void testAsDefault() throws Exception {
		assertThat(string("text").as(Void.class), nullValue());
	}
	
	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);

		string("text").apply(visitor, scope);
		
		verify(visitor).visitStringLiteral(string("text"), scope);
	}

	@Test
	public void testEquals() throws Exception {
		assertThat(string("text"), satisfiesDefaultEquality()
			.andEqualTo(string("text"))
			.andNotEqualTo(string("other text")));
	}

	@Test
	public void testToString() throws Exception {
		assertThat(string("string").toString(), equalTo("'string'"));
	}

}
