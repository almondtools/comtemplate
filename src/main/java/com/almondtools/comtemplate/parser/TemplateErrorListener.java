package com.almondtools.comtemplate.parser;

import static java.util.stream.IntStream.rangeClosed;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.atn.ATNConfig;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.atn.Transition;
import org.antlr.v4.runtime.misc.IntervalSet;

public class TemplateErrorListener extends BaseErrorListener {

	private static final String UNEXPECTED = "unexpected characters in line ";
	private static final String ERROR = "error in line ";
	private static final String INVALID = "invalid syntax in line ";
	
	private List<String> messages;

	public TemplateErrorListener() {
		messages = new ArrayList<String>();
	}

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
		if (e instanceof InputMismatchException) {
			messages.add(ERROR + pos(line, charPositionInLine) + " " + msg);
		} else if (e instanceof FailedPredicateException) {
			messages.add(INVALID + pos(line, charPositionInLine) + " <" + e.getCtx().getText() + '>');
		} else if (e instanceof LexerNoViableAltException) {
			messages.add(UNEXPECTED + pos(line, charPositionInLine) + message((LexerNoViableAltException) e));
		} else if (e instanceof NoViableAltException) {
			messages.add(UNEXPECTED + pos(line, charPositionInLine) + message((NoViableAltException) e));
		} else if (e instanceof TemplateSyntaxException) {
			messages.add(INVALID + pos(line, charPositionInLine) + " <" + e.getCtx().getText() + '>');
		} else {
			messages.add(ERROR + pos(line, charPositionInLine) + " " + msg);

		}
	}

	private String pos(int line, int charPositionInLine) {
		return line + ":" + charPositionInLine;
	}

	private String message(NoViableAltException e) {
		return " was <" + found(e) + ">, expected " + expected(e.getRecognizer(), tokens(e.getDeadEndConfigs()));
	}

	private String message(LexerNoViableAltException e) {
		return " was <" + found(e) + ">, expected " + expected(e.getRecognizer(), tokens(e.getDeadEndConfigs()));
	}

	private IntervalSet tokens(ATNConfigSet configs) {
		IntervalSet tokens = new IntervalSet();
		for (ATNConfig config : configs) {
			for (Transition transition : config.state.getTransitions()) {
				tokens .addAll(transition.label());
			}
		}
		return tokens;
	}

	private String found(RecognitionException e) {
		Token token = e.getOffendingToken();
		return token.getText();
	}

	private String expected(Recognizer<?, ?> recognizer, IntervalSet expectedTokens) {
		
		Vocabulary vocabulary = recognizer.getVocabulary();
		return expectedTokens.getIntervals().stream()
			.flatMap(interval -> (Stream<String>) rangeClosed(interval.a, interval.b)
				.mapToObj(item -> vocabulary.getDisplayName(item)))
			.collect(Collectors.joining(", ", "<", ">"));
	}

	public List<String> getMessages() {
		return messages;
	}

	public boolean isSuccesful() {
		return messages.isEmpty();
	}
}