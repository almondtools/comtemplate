package com.almondtools.util.stream;

import static com.almondtools.util.stream.Unescaper.defaultUnescaper;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.almondtools.util.stream.Unescaper;

public class UnescaperTest {

	@Test(expected=IllegalArgumentException.class)
	public void testUnescaperWithIllegalNumberOfChars() throws Exception {
		defaultUnescaper('n', '\n', 'r');
	}

	@Test
	public void testConsumeCharNoEscapes() throws Exception {
		Unescaper unescaper = defaultUnescaper();
		unescaper.consume('a');
		unescaper.consume('\\');
		unescaper.consume('n');
		unescaper.consume('b');
		assertThat(unescaper.toString(), equalTo("anb"));
	}

	@Test
	public void testConsumeChar() throws Exception {
		Unescaper unescaper = defaultUnescaper('n', '\n');
		unescaper.consume('a');
		unescaper.consume('b');
		assertThat(unescaper.toString(), equalTo("ab"));
	}

	@Test
	public void testConsumeEscape() throws Exception {
		Unescaper unescaper = defaultUnescaper('n', '\n');
		unescaper.consume('a');
		unescaper.consume('\\');
		unescaper.consume('n');
		unescaper.consume('b');
		assertThat(unescaper.toString(), equalTo("a\nb"));
	}

	@Test
	public void testConsumeUnicodeEscape() throws Exception {
		Unescaper unescaper = defaultUnescaper('n', '\n', 'u', '\u0000');
		unescaper.consume('a');
		unescaper.consume('\\');
		unescaper.consume('n');
		unescaper.consume('\\');
		unescaper.consume('u');
		unescaper.consume('0');
		unescaper.consume('0');
		unescaper.consume('6');
		unescaper.consume('2');
		assertThat(unescaper.toString(), equalTo("a\nb"));
	}

	@Test
	public void testConsumeChangedEscaper() throws Exception {
		Unescaper unescaper = Unescaper.customUnescaper('/', 'n', '\n');
		unescaper.consume('a');
		unescaper.consume('/');
		unescaper.consume('n');
		unescaper.consume('b');
		assertThat(unescaper.toString(), equalTo("a\nb"));
	}

	@Test
	public void testJoin() throws Exception {
		Unescaper a = defaultUnescaper('n', '\n');
		a.consume('a');
		Unescaper b = defaultUnescaper('n', '\n');
		a.consume('b');
		a.join(b);
		assertThat(a.toString(), equalTo("ab"));
	}

}
