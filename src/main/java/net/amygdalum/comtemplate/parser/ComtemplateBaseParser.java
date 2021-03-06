package net.amygdalum.comtemplate.parser;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

public abstract class ComtemplateBaseParser extends Parser {

	public ComtemplateBaseParser(TokenStream input) {
		super(input);
		setErrorHandler(new ComtemplateErrorStrategy());
	}

	public void enable(int channel) {
		if (_input instanceof MultiChannelTokenStream) {
			((MultiChannelTokenStream) _input).enable(channel);
		}
	}
	
	public void disable(int channel) {
		if (_input instanceof MultiChannelTokenStream) {
			((MultiChannelTokenStream) _input).disable(channel);
		}
	}

}
