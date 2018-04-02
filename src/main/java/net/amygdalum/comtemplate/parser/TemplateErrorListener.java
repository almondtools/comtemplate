package net.amygdalum.comtemplate.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class TemplateErrorListener extends BaseErrorListener {

	private static final String ERROR = "error at ";

	private List<String> messages;

	public TemplateErrorListener() {
		messages = new ArrayList<String>();
	}

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
		messages.add(ERROR + pos(line, charPositionInLine) + " " + msg);
	}

	private String pos(int line, int charPositionInLine) {
		return line + ":" + charPositionInLine;
	}

	public List<String> getMessages() {
		return messages;
	}

	public boolean isSuccesful() {
		return messages.isEmpty();
	}
}