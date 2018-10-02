package net.amygdalum.comtemplate.parser;

import static java.util.stream.IntStream.rangeClosed;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.atn.ATNConfig;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.atn.Transition;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.tree.ParseTree;

public class ComtemplateErrorStrategy extends DefaultErrorStrategy {

	private int lastErrorIndex;

	@Override
	public void reportError(Parser recognizer, RecognitionException e) {
		if (inErrorRecoveryMode(recognizer)) {
			return;
		}
		if (e == null) {
			reportNullError(recognizer);
			return;
		}

		beginErrorCondition(recognizer);

		if (e instanceof NoViableAltException) {
			reportNoViableAlternative(recognizer, (NoViableAltException) e);
		} else if (e instanceof InputMismatchException) {
			reportInputMismatch(recognizer, (InputMismatchException) e);
		} else if (e instanceof FailedPredicateException) {
			reportFailedPredicate(recognizer, (FailedPredicateException) e);
		} else {
			reportOtherError(recognizer, e);
		}
	}

	private void reportNullError(Parser recognizer) {
		Token t = recognizer.getCurrentToken();
		if (isIndependentError(recognizer, t)) {
			recognizer.notifyErrorListeners(t, "unexpected token <" + found(t) + ">, expected " + expected(recognizer, getExpectedTokens(recognizer)), null);
		}
	}

	private void reportOtherError(Parser recognizer, RecognitionException e) {
		Token t = e.getOffendingToken();
		if (isIndependentError(recognizer, t)) {
			recognizer.notifyErrorListeners(t, "unexpected token <" + found(t) + ">, expected " + expected(recognizer, e.getExpectedTokens()), e);
		}
	}

	@Override
	protected void reportFailedPredicate(Parser recognizer, FailedPredicateException e) {
		Token t = e.getOffendingToken();
		if (isIndependentError(recognizer, t)) {
			recognizer.notifyErrorListeners(t, "unexpected token <" + found(e) + ">, expected " + expected(e.getRecognizer(), e.getExpectedTokens()), e);
		}
	}

	@Override
	protected void reportInputMismatch(Parser recognizer, InputMismatchException e) {
		Token t = e.getOffendingToken();
		if (isIndependentError(recognizer, t)) {
			recognizer.notifyErrorListeners(t, "unexpected token <" + found(e) + ">, expected " + expected(e.getRecognizer(), e.getExpectedTokens()), e);
		}
	}

	@Override
	protected void reportMissingToken(Parser recognizer) {
		if (inErrorRecoveryMode(recognizer)) {
			return;
		}

		beginErrorCondition(recognizer);

		Token t = recognizer.getCurrentToken();
		if (lastErrorIndex >= t.getTokenIndex()) {
			return;
		}
		lastErrorIndex = t.getTokenIndex();
		recognizer.notifyErrorListeners(t, "unexpected token <" + found(t) + ">, expected " + expected(recognizer, getExpectedTokens(recognizer)), null);
	}

	@Override
	protected void reportNoViableAlternative(Parser recognizer, NoViableAltException e) {
		Token t = e.getOffendingToken();
		if (isIndependentError(recognizer, t)) {
			recognizer.notifyErrorListeners(t, "unexpected token <" + found(e) + ">, expected " + expected(e.getRecognizer(), tokens(e.getDeadEndConfigs())), e);
		}
	}

	@Override
	protected void reportUnwantedToken(Parser recognizer) {
		if (inErrorRecoveryMode(recognizer)) {
			return;
		}

		beginErrorCondition(recognizer);

		Token t = recognizer.getCurrentToken();
		if (isIndependentError(recognizer, t)) {
			recognizer.notifyErrorListeners(t, "unexpected token <" + found(t) + ">, expected " + expected(recognizer, getExpectedTokens(recognizer)), null);
		}
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

	private boolean isIndependentError(Parser recognizer, Token t) {
		if (lastErrorIndex >= t.getTokenIndex()) {
			return false;
		}
		ParserRuleContext ctx = recognizer.getRuleContext();
		ParserRuleContext parent = ctx.getParent();
		while (parent != null && parent.getChildCount() < 2) {
			parent = parent.getParent();
		}
		if (parent != null) {
			ParseTree previous = parent.getChild(parent.getChildCount() -2);
			while (previous != null && previous.getChildCount() > 0) {
				if (previous instanceof ParserRuleContext) {
					if (((ParserRuleContext) previous).exception != null) {
						return false;
					}
				}
				previous = previous.getChild(previous.getChildCount()-1);
			}
		}
		lastErrorIndex = t.getTokenIndex();
		return true;
	}
	
	
}
