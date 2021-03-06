package net.amygdalum.comtemplate.engine.expressions;

import static com.almondtools.conmatch.conventions.EqualityMatcher.satisfiesDefaultEquality;
import static java.util.Collections.emptyList;
import static net.amygdalum.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.CustomTemplateDefinition;
import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;


public class RawTextTest {

	@Test
	public void testEvaluateWithConstantText() throws Exception {
		CustomTemplateDefinition definition = new CustomTemplateDefinition("name", emptyList());
		definition.add(new RawText("text"));

		String evaluated = definition.evaluate(interpreter(), emptyList());
		assertThat(evaluated, equalTo("text"));
	}

	@Test
	public void testGetText() throws Exception {
		assertThat(new RawText("string").getText(), equalTo("string"));
	}

	@Test
	public void testAsString() throws Exception {
		assertThat(new RawText("string").as(String.class), equalTo("string"));
	}

	@Test
	public void testAsDefault() throws Exception {
		assertThat(new RawText("string").as(Void.class), nullValue());
	}
	
	@Test
	public void testJoin() throws Exception {
		RawText rawText = new RawText("a");
		rawText.join(new RawText("b"));
		assertThat(rawText, equalTo(new RawText("ab")));
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);
		RawText literal = new RawText("text");

		literal.apply(visitor, scope);

		verify(visitor).visitRawText(literal, scope);
	}

	@Test
	public void testEquals() throws Exception {
		assertThat(new RawText("text"), satisfiesDefaultEquality()
			.andEqualTo(new RawText("text"))
			.andNotEqualTo(new RawText("corrupt text"))
			.andNotEqualTo(new RawText("no text")));
	}

	@Test
	public void testToString() throws Exception {
		assertThat(new RawText("string").toString(), equalTo("'string'"));
	}

}
