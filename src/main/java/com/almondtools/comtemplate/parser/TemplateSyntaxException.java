package com.almondtools.comtemplate.parser;

import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class TemplateSyntaxException extends RecognitionException {

	public TemplateSyntaxException(Recognizer<?, ?> recognizer, IntStream input, ParserRuleContext ctx) {
		super(recognizer, input, ctx);
		setOffendingToken(ctx.getStart());
	}

}
