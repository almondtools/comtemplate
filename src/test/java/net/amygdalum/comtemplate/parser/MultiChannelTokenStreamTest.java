package net.amygdalum.comtemplate.parser;

import static com.almondtools.conmatch.datatypes.PrimitiveArrayMatcher.intArrayContaining;
import static net.amygdalum.xrayinterface.XRayInterface.xray;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;
import org.junit.jupiter.api.Test;

import net.amygdalum.comtemplate.parser.MultiChannelTokenStream;

public class MultiChannelTokenStreamTest {

	@Test
	public void testInitialChannels() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource);
		assertThat(xray(stream).to(OpenChannel.class).getChannels(), intArrayContaining(0));
	}

	@Test
	public void testEnable() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource);
		stream.enable(4);
		assertThat(xray(stream).to(OpenChannel.class).getChannels(), intArrayContaining(0, 4));
	}

	@Test
	public void testDisable() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource);
		stream.enable(4);
		stream.disable(0);
		assertThat(xray(stream).to(OpenChannel.class).getChannels(), intArrayContaining(4));
	}

	@Test
	public void testEnableTwice() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource);
		stream.enable(4);
		stream.enable(5);
		stream.enable(4);
		assertThat(xray(stream).to(OpenChannel.class).getChannels(), intArrayContaining(0, 4, 5));
	}

	@Test
	public void testEnableRestoresSkippedChannel() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		when(tokenSource.nextToken()).thenReturn(token(0), token(0), token(4), token(1), token(4), new CommonToken(Token.EOF));
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource);
		stream.fill();
		stream.seek(5);
		assertThat(stream.index(), equalTo(5));

		stream.enable(4);
		assertThat(stream.index(), equalTo(2));
	}

	@Test
	public void testNextTokenOnChannel() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		when(tokenSource.nextToken()).thenReturn(token(0), token(0), token(4), token(1), token(4), new CommonToken(Token.EOF));
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource);
		assertThat(stream.nextTokenOnChannel(0, new int[] { 0 }), equalTo(0));
		assertThat(stream.nextTokenOnChannel(0, new int[] { 4 }), equalTo(2));
		assertThat(stream.nextTokenOnChannel(0, new int[] { 1 }), equalTo(3));
		assertThat(stream.nextTokenOnChannel(0, new int[] { 0, 4 }), equalTo(0));
		assertThat(stream.nextTokenOnChannel(0, new int[] { 1, 4 }), equalTo(2));
		assertThat(stream.nextTokenOnChannel(5, new int[] { 1, 4 }), equalTo(5));
		assertThat(stream.nextTokenOnChannel(6, new int[] { 1, 4 }), equalTo(5));
	}

	@Test
	public void testPreviousTokenOnChannel() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		when(tokenSource.nextToken()).thenReturn(token(0), token(0), token(4), token(1), token(4), new CommonToken(Token.EOF));
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource);
		assertThat(stream.previousTokenOnChannel(0, new int[] { 0 }), equalTo(0));
		assertThat(stream.previousTokenOnChannel(0, new int[] { 1 }), equalTo(-1));
		assertThat(stream.previousTokenOnChannel(5, new int[] { 0 }), equalTo(5));
		assertThat(stream.previousTokenOnChannel(6, new int[] { 0 }), equalTo(5));
	}

	@Test
	public void testLB() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		when(tokenSource.nextToken()).thenReturn(token(0), token(0), token(4), token(1), token(4), new CommonToken(Token.EOF));
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource);
		stream.enable(4);
		stream.enable(1);
		stream.seek(4);
		assertThat(stream.LB(0), nullValue());
		assertThat(stream.LB(1).getChannel(), equalTo(1));
		assertThat(stream.LB(2).getChannel(), equalTo(4));
		assertThat(stream.LB(3).getChannel(), equalTo(0));
		assertThat(stream.LB(4).getChannel(), equalTo(0));
		assertThat(stream.LB(5), nullValue());
	}
	
	@Test
	public void testLBselectedChannels() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		when(tokenSource.nextToken()).thenReturn(token(0), token(0), token(4), token(1), token(4), new CommonToken(Token.EOF));
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource);
		stream.enable(1);
		stream.seek(4);
		assertThat(stream.LB(0), nullValue());
		assertThat(stream.LB(1).getChannel(), equalTo(1));
		assertThat(stream.LB(2).getChannel(), equalTo(0));
		assertThat(stream.LB(3).getChannel(), equalTo(0));
		assertThat(stream.LB(4), nullValue());
	}
	
	@Test
	public void testLT() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		when(tokenSource.nextToken()).thenReturn(token(0), token(0), token(4), token(1), token(4), new CommonToken(Token.EOF));
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource);
		stream.enable(4);
		stream.enable(1);
		stream.seek(0);
		assertThat(stream.LT(0), nullValue());
		assertThat(stream.LT(1).getChannel(), equalTo(0));
		assertThat(stream.LT(2).getChannel(), equalTo(0));
		assertThat(stream.LT(3).getChannel(), equalTo(4));
		assertThat(stream.LT(4).getChannel(), equalTo(1));
		assertThat(stream.LT(5).getChannel(), equalTo(4));
		assertThat(stream.LT(6).getType(), equalTo(Token.EOF));
		assertThat(stream.LT(7).getType(), equalTo(Token.EOF));
	}
	
	@Test
	public void testGetNumberOfOnChannelTokens0() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		when(tokenSource.nextToken()).thenReturn(token(0), token(0), token(4), token(1), token(4), new CommonToken(Token.EOF));
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource);
		stream.enable(0);

		assertThat(stream.getNumberOfOnChannelTokens(), equalTo(3));
	}
	
	@Test
	public void testGetNumberOfOnChannelTokens1() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		when(tokenSource.nextToken()).thenReturn(token(0), token(0), token(4), token(1), token(4), new CommonToken(Token.EOF));
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource);
		stream.disable(0);
		stream.enable(1);
		
		assertThat(stream.getNumberOfOnChannelTokens(), equalTo(1));
	}
	
	@Test
	public void testGetNumberOfOnChannelTokens4() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		when(tokenSource.nextToken()).thenReturn(token(0), token(0), token(4), token(1), token(4), new CommonToken(Token.EOF));
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource);
		stream.disable(0);
		stream.enable(4);
		
		assertThat(stream.getNumberOfOnChannelTokens(), equalTo(2));
	}
	
	@Test
	public void testGetNumberOfOnChannelTokens014() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		when(tokenSource.nextToken()).thenReturn(token(0), token(0), token(4), token(1), token(4), new CommonToken(Token.EOF));
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource);
		stream.enable(0);
		stream.enable(1);
		stream.enable(4);

		assertThat(stream.getNumberOfOnChannelTokens(), equalTo(6));
	}

	@Test
	public void testGetNumberOfOnChannelTokensEmptySource() throws Exception {
		TokenSource tokenSource = mock(TokenSource.class);
		when(tokenSource.nextToken()).thenReturn(new CommonToken(Token.EOF));
		MultiChannelTokenStream stream = new MultiChannelTokenStream(tokenSource) {
			@Override
			public void fill() {
			}
		};

		assertThat(stream.getNumberOfOnChannelTokens(), equalTo(0));
	}

	private Token token(int channel) {
		CommonToken token = new CommonToken(42);
		token.setChannel(channel);
		return token;
	}

	interface OpenChannel {
		int[] getChannels();
	}

}
