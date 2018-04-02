package net.amygdalum.comtemplate.parser;

import static java.util.stream.IntStream.rangeClosed;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.atn.ATNConfig;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.atn.Transition;
import org.antlr.v4.runtime.misc.IntervalSet;

public class ComtemplateErrorStrategy extends DefaultErrorStrategy {

	@Override
	public void reportError(Parser recognizer, RecognitionException e) {
		if (inErrorRecoveryMode(recognizer)) {
			return;
		}
		beginErrorCondition(recognizer);

		if (e instanceof NoViableAltException) {
			reportNoViableAlternative(recognizer, (NoViableAltException) e);
		} else if (e instanceof InputMismatchException) {
			reportInputMismatch(recognizer, (InputMismatchException) e);
		} else if (e instanceof FailedPredicateException) {
			reportFailedPredicate(recognizer, (FailedPredicateException) e);
		} else if (e instanceof TemplateSyntaxException) {
			Token t = e.getOffendingToken();
			recognizer.notifyErrorListeners(t, "unexpected token <" + e.getCtx().getText() + '>', e);
		} else if (e != null) {
			Token t = e.getOffendingToken();
			recognizer.notifyErrorListeners(t, "unexpected token <" + found(t) + ">, expected " + expected(recognizer, e.getExpectedTokens()), e);
		} else {
			Token t = recognizer.getCurrentToken();
			IntervalSet expecting = getExpectedTokens(recognizer);
			recognizer.notifyErrorListeners(t, "unexpected token <" + found(t) + ">, expected " + expected(recognizer, expecting), e);
		}
	}

	@Override
	protected void reportFailedPredicate(Parser recognizer, FailedPredicateException e) {
		recognizer.notifyErrorListeners(e.getOffendingToken(), "unexpected token <" + found(e) + ">, expected " + expected(e.getRecognizer(), e.getExpectedTokens()), e);
	}

	@Override
	protected void reportInputMismatch(Parser recognizer, InputMismatchException e) {
		recognizer.notifyErrorListeners(e.getOffendingToken(), "unexpected token <" + found(e) + ">, expected " + expected(e.getRecognizer(), e.getExpectedTokens()), e);
	}

	@Override
	protected void reportMissingToken(Parser recognizer) {
		if (inErrorRecoveryMode(recognizer)) {
			return;
		}

		beginErrorCondition(recognizer);

		Token t = recognizer.getCurrentToken();
		IntervalSet expecting = getExpectedTokens(recognizer);
		recognizer.notifyErrorListeners(t, "unexpected token <" + found(t) + ">, expected " + expected(recognizer, expecting), null);
	}

	@Override
	protected void reportNoViableAlternative(Parser recognizer, NoViableAltException e) {
		recognizer.notifyErrorListeners(e.getOffendingToken(), "unexpected characters <" + found(e) + ">, expected " + expected(e.getRecognizer(), tokens(e.getDeadEndConfigs())), e);
	}

	@Override
	protected void reportUnwantedToken(Parser recognizer) {
		if (inErrorRecoveryMode(recognizer)) {
			return;
		}

		beginErrorCondition(recognizer);

		Token t = recognizer.getCurrentToken();
		IntervalSet expecting = getExpectedTokens(recognizer);
		recognizer.notifyErrorListeners(t, "unexpected token <" + found(t) + ">, expected " + expected(recognizer, expecting), null);
	}

	private IntervalSet tokens(ATNConfigSet configs) {
		IntervalSet tokens = new IntervalSet();
		for (ATNConfig config : configs) {
			for (Transition transition : config.state.getTransitions()) {
				tokens.addAll(transition.label());
			}
		}
		return tokens;
	}

	private String found(RecognitionException e) {
		Token token = e.getOffendingToken();
		return found(token);
	}

	public String found(Token token) {
		return token.getText();
	}

	private String expected(Recognizer<?, ?> recognizer, IntervalSet expectedTokens) {
		Vocabulary vocabulary = recognizer.getVocabulary();
		return expectedTokens.getIntervals().stream()
			.flatMap(interval -> (Stream<String>) rangeClosed(interval.a, interval.b)
				.mapToObj(item -> vocabulary.getDisplayName(item)))
			.collect(Collectors.joining(", ", "<", ">"));
	}

}
