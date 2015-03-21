package com.almondtools.comtemplate.engine.expressions;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class ResolutionErrorsTest {

	@Test
	public void testGetMessageEmpty() throws Exception {
		ResolutionErrors error = new ResolutionErrors(emptyList());
		
		assertThat(error.getMessage(), isEmptyString());
	}

	@Test
	public void testGetMessageSingle() throws Exception {
		ResolutionErrors error = new ResolutionErrors(asList(new TestError()));

		assertThat(error.getMessage(), equalTo("test"));
	}
	
	@Test
	public void testGetMessageMultiple() throws Exception {
		ResolutionErrors error = new ResolutionErrors(asList(new TestError("test1"), new TestError("test2")));

		assertThat(error.getMessage(), containsString("test1"));
		assertThat(error.getMessage(), containsString("test2"));
	}
	
}
