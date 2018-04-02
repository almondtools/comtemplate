package net.amygdalum.comtemplate.engine.expressions;

import static com.almondtools.conmatch.conventions.EqualityMatcher.satisfiesDefaultEquality;
import static net.amygdalum.comtemplate.engine.expressions.DecimalLiteral.decimal;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.TemplateExpressionVisitor;


public class DecimalLiteralTest {

	@Test
	public void testGetValue() throws Exception {
		assertThat(decimal(-0.8).getValue(), equalTo(BigDecimal.valueOf(-0.8)));
	}

	@Test
	public void testGetText() throws Exception {
		assertThat(decimal(21).getText(), equalTo("21.0"));
	}

	@Test
	public void testAsBigDecimal() throws Exception {
		assertThat(decimal("2.71").as(BigDecimal.class), equalTo(BigDecimal.valueOf(2.71)));
	}

	@Test
	public void testAsDouble() throws Exception {
		assertThat(decimal(2.71).as(Double.class), equalTo(new Double(2.71)));
	}
	
	@Test
	public void testAsFloat() throws Exception {
		assertThat(decimal(2.71).as(Float.class), equalTo(new Float(2.71)));
	}
	
	@Test
	public void testAsDefault() throws Exception {
		assertThat(decimal(2.71).as(Void.class), nullValue());
	}

	@Test
	public void testApply() throws Exception {
		TemplateExpressionVisitor<?> visitor = mock(TemplateExpressionVisitor.class);
		Scope scope = mock(Scope.class);

		decimal(2.71).apply(visitor, scope);
		
		verify(visitor).visitDecimalLiteral(decimal(2.71), scope);
	}

	@Test
	public void testEquals() throws Exception {
		assertThat(decimal(3.14), satisfiesDefaultEquality()
			.andEqualTo(decimal(BigDecimal.valueOf(3.14)))
			.andEqualTo(decimal(3.14))
			.andEqualTo(decimal("3.14"))
			.andNotEqualTo(decimal("-3.14")));
	}

	@Test
	public void testToString() throws Exception {
		assertThat(decimal(3.14).toString(), equalTo("3.14"));
	}

}
