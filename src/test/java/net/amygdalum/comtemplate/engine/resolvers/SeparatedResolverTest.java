package net.amygdalum.comtemplate.engine.resolvers;

import static java.util.Arrays.asList;
import static net.amygdalum.comtemplate.engine.expressions.StringLiteral.string;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.Scope;
import net.amygdalum.comtemplate.engine.expressions.RawText;
import net.amygdalum.comtemplate.engine.expressions.ResolvedListLiteral;
import net.amygdalum.comtemplate.engine.resolvers.SeparatedResolver;

public class SeparatedResolverTest {

	private SeparatedResolver resolver;

	@BeforeEach
	public void before() {
		resolver = new SeparatedResolver();
	}

	@Test
	public void testResolveList() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(new ResolvedListLiteral(string("a"), string("b")), asList(string(":")), scope), equalTo(new RawText("a:b")));
	}

	@Test
	public void testResolveOther() throws Exception {
		Scope scope = mock(Scope.class);

		assertThat(resolver.resolve(string("ab"), asList(string(":")), scope), equalTo(string("ab")));
	}

}
