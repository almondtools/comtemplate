package com.almondtools.comtemplate.engine.resolvers;

import static com.almondtools.comtemplate.engine.resolvers.Normalizations.compact;
import static java.util.regex.Pattern.MULTILINE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.almondtools.comtemplate.engine.Scope;
import com.almondtools.comtemplate.engine.TemplateImmediateExpression;
import com.almondtools.comtemplate.engine.expressions.Evaluated;
import com.almondtools.comtemplate.engine.expressions.RawText;

public class IndentResolver extends FunctionResolver {

	private static final String LINE_BREAK = "(\\r\\n?|\\n)";
	private static final Pattern INDENT0 = Pattern.compile(LINE_BREAK, MULTILINE);
	private static final Pattern INDENT = Pattern.compile(LINE_BREAK + "( +)(?![\\r\\n])", MULTILINE);
	private static final Pattern LAST_INDENT = Pattern.compile(LINE_BREAK + "( *)\\z", MULTILINE);

	public IndentResolver() {
		super("indent", 0);
	}

	@Override
	public TemplateImmediateExpression resolve(TemplateImmediateExpression base, List<TemplateImmediateExpression> arguments, Scope scope) {
		if (base instanceof RawText) {
			return indentRawText((RawText) base);
		} else if (base instanceof Evaluated) {
			return indentEvaluated((Evaluated) base);
		} else {
			return base;
		}
	}

	private RawText indentRawText(RawText rawText) {
		RawText withoutCommonIndentation = clearCommonIndentation(rawText, true);
		return withoutCommonIndentation;
	}

	private RawText clearCommonIndentation(RawText rawText, boolean first) {
		int commonIndentation = findCommonIndentation(rawText, first);
		if (commonIndentation < 1) {
			return rawText;
		} else {
			return clearCommonIndentation(rawText, commonIndentation, first);
		}
	}

	private RawText clearCommonIndentation(RawText rawText, int commonIndentation, boolean first) {
		String text = rawText.getText();
		Pattern varIndent = Pattern.compile(LINE_BREAK + " {" + commonIndentation + "}");
		Matcher m = varIndent.matcher(matchText(text, first));
		String clearedText = m.replaceAll("$1");
		if (first) {
			clearedText = clearedText.substring(1);
		}
			
		return new RawText(clearedText);
	}

	private int findCommonIndentation(RawText rawText, boolean first) {
		String text = rawText.getText();
		Matcher m = INDENT.matcher(matchText(text, first));
		int commonIndentation = Integer.MAX_VALUE;
		while (m.find()) {
			String indentSpaces = m.group(2);
			int indentation = indentSpaces.length();
			commonIndentation = commonIndentation < indentation ? commonIndentation : indentation;
		}
		if (commonIndentation == Integer.MAX_VALUE) {
			return -1;
		} else {
			return commonIndentation;
		}
	}

	private Evaluated indentEvaluated(Evaluated evaluated) {
		List<TemplateImmediateExpression> expressions = compact(evaluated.getEvaluated());
		expressions = clearCommonIndentation(expressions, true);
		expressions = propagateIndentation(expressions);
		return new Evaluated(expressions);
	}

	private List<TemplateImmediateExpression> clearCommonIndentation(List<TemplateImmediateExpression> expressions, boolean first) {
		expressions = clearPartsOfCommonIndentation(expressions, first);

		int commonIndentation = findCommonIndentation(expressions, first);
		if (commonIndentation >= 0) {
			expressions = clearCommonIndentation(expressions, commonIndentation, first);
		}

		return expressions;
	}

	private List<TemplateImmediateExpression> clearPartsOfCommonIndentation(List<TemplateImmediateExpression> expressions, boolean first) {
		List<TemplateImmediateExpression> clearedExpressions = new ArrayList<>();
		for (TemplateImmediateExpression expression : expressions) {
			if (expression instanceof Evaluated) {
				clearedExpressions.add(new Evaluated(clearCommonIndentation(((Evaluated) expression).getEvaluated(), first)));
				first = false;
			} else if (expression instanceof RawText) {
				clearedExpressions.add(expression);
				int lastIndent = computeLastIndent((RawText) expression);
				first = lastIndent >= 0;
			} else {
				clearedExpressions.add(expression);
				first = false;
			}
		}
		return clearedExpressions;
	}

	private int findCommonIndentation(List<TemplateImmediateExpression> expressions, boolean first) {
		int commonIndentation = Integer.MAX_VALUE;
		for (TemplateImmediateExpression expression : expressions) {
			if (expression instanceof RawText) {
				int indentation = findCommonIndentation((RawText) expression, first);
				if (indentation >= 0) {
					commonIndentation = commonIndentation < indentation ? commonIndentation : indentation;
				}
			}
			first = false;
		}
		if (commonIndentation == Integer.MAX_VALUE) {
			return -1;
		} else {
			return commonIndentation;
		}
	}

	private List<TemplateImmediateExpression> clearCommonIndentation(List<TemplateImmediateExpression> expressions, int commonIndentation, boolean first) {
		List<TemplateImmediateExpression> clearedExpressions = new ArrayList<>();
		for (TemplateImmediateExpression expression : expressions) {
			if (expression instanceof RawText) {
				clearedExpressions.add(clearCommonIndentation((RawText) expression, commonIndentation, first));
			} else {
				clearedExpressions.add(expression);
			}
			first = false;
		}
		return clearedExpressions;
	}

	private List<TemplateImmediateExpression> propagateIndentation(List<TemplateImmediateExpression> expressions) {
		List<TemplateImmediateExpression> propagatedExpressions = propagateIndentationBase(expressions);
		propagatedExpressions = propagateIndentationRecursive(propagatedExpressions);
		return propagatedExpressions;
	}

	public List<TemplateImmediateExpression> propagateIndentationBase(List<TemplateImmediateExpression> expressions) {
		List<TemplateImmediateExpression> propagatedExpressions = new ArrayList<>();
		RawText lastText = null;
		for (TemplateImmediateExpression expression : expressions) {
			if (expression instanceof Evaluated && lastText != null) {
				int lastIndentation = computeLastIndent(lastText);
				if (lastIndentation >= 0) {
					propagatedExpressions.add(stripLastIndentation(lastText, lastIndentation));
					propagatedExpressions.add(propagateIndentation((Evaluated) expression, lastIndentation));
				} else {
					propagatedExpressions.add(lastText);
					propagatedExpressions.add(expression);
				}
				lastText = null;
			} else {
				if (lastText != null) {
					propagatedExpressions.add(lastText);
					lastText = null;
				}
				if (expression instanceof RawText) {
					lastText = (RawText) expression;
				} else {
					propagatedExpressions.add(expression);
				}
			}
		}
		if (lastText != null) {
			propagatedExpressions.add(lastText);
			lastText = null;
		}
		return propagatedExpressions;
	}

	private List<TemplateImmediateExpression> propagateIndentationRecursive(List<TemplateImmediateExpression> expressions) {
		List<TemplateImmediateExpression> propagatedExpressions = new ArrayList<>();
		for (TemplateImmediateExpression expression : expressions) {
			if (expression instanceof Evaluated) {
				propagatedExpressions.add(new Evaluated(propagateIndentation(((Evaluated) expression).getEvaluated())));
			} else {
				propagatedExpressions.add(expression);
			}
		}
		return propagatedExpressions;
	}

	private int computeLastIndent(RawText rawText) {
		String text = rawText.getText();
		Matcher m = LAST_INDENT.matcher('\n' + text);
		if (m.find()) {
			return m.group(2).length();
		} else {
			return -1;
		}
	}

	private TemplateImmediateExpression stripLastIndentation(RawText rawText, int indentation) {
		String text = rawText.getText();
		return new RawText(text.substring(0, text.length() - indentation));
	}

	private Evaluated propagateIndentation(Evaluated evaluated, int indentation) {
		List<TemplateImmediateExpression> expressions = new ArrayList<>();
		boolean first = true;
		for (TemplateImmediateExpression expression : evaluated.getEvaluated()) {
			if (expression instanceof RawText) {
				expressions.add(propagateIndentation((RawText) expression, indentation, first));
			} else if (first) {
				expressions.add(new RawText(spaces(indentation)));
				expressions.add(expression);
			} else {
				expressions.add(expression);
			}
			first = false;
		}
		return new Evaluated(expressions);
	}

	private RawText propagateIndentation(RawText rawText, int indentation, boolean first) {
		String origText = rawText.getText();
		Matcher m = INDENT0.matcher(origText);
		String newText = m.replaceAll("$1" + spaces(indentation));
		if (first) {
			newText = spaces(indentation) + newText;
		}
		return new RawText(newText);
	}

	private String spaces(int size) {
		char[] indentSpaces = new char[size];
		Arrays.fill(indentSpaces, ' ');
		return new String(indentSpaces);
	}

	private String matchText(String text, boolean first) {
		if (first) {
			return '\n' + text;
		} else {
			return text;
		}
	}

}
