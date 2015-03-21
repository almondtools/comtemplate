package com.almondtools.comtemplate.engine.expressions;

import static java.util.stream.Collectors.joining;

import java.util.List;

public class ResolutionErrors extends ErrorExpression {

	private List<ErrorExpression> errors;

	public ResolutionErrors(List<ErrorExpression> errors) {
		this.errors = errors;
	}

	@Override
	public String getMessage() {
		if (errors.isEmpty()) {
			return "";
		} else if (errors.size() == 1) {
			return errors.get(0).getMessage();
		} else {
			return "multiple errors occured:\n" + errors.stream()
				.map(error -> error.getMessage())
				.collect(joining("\n"));
		}
	}

}
