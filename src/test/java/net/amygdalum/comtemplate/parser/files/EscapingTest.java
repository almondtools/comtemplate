package net.amygdalum.comtemplate.parser.files;

import static net.amygdalum.comtemplate.engine.TestTemplateIntepreter.interpreter;
import static net.amygdalum.comtemplate.parser.files.TemplateTests.compileLibrary;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.engine.TemplateGroup;

public class EscapingTest {

    private TemplateGroup group;

    @BeforeEach
    public void before() throws Exception {
        group = compileLibrary("src/test/resources/escaping.ctp");
    }

    @Test
    public void testEscapedExample1() throws Exception {
        String rendered = group.getDefinition("escapedExample1").evaluate(interpreter());
        assertThat(rendered, equalTo("<<escaped>>"));
    }

    @Test
    public void testEscapedExample2() throws Exception {
        String rendered = group.getDefinition("escapedExample2").evaluate(interpreter());
        assertThat(rendered, equalTo("<<escaped>>"));
    }

    @Test
    public void testEscapedExample3() throws Exception {
        String rendered = group.getDefinition("escapedExample3").evaluate(interpreter());
        assertThat(rendered, equalTo("@<escaped>"));
    }

    @Test
    public void testNotEscapedExample() throws Exception {
        String rendered = group.getDefinition("notescapedExample").evaluate(interpreter());
        assertThat(rendered, equalTo("<notescaped>"));
    }

    @Test
    public void testEscapedHtml() throws Exception {
        String rendered = group.getDefinition("escapedHtml").evaluate(interpreter());
        assertThat(rendered, equalTo("<<<html>myhtml</html>>>"));
    }
}
