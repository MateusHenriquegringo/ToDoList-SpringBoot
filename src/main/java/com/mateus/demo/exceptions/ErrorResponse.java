package com.mateus.demo.exceptions;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
	private final int status;
	private final String message;
	private String stackTrace;
	private List<ValidationError> errors;

	public void addValidationError(String field, String message) {
		if (Objects.isNull(errors)) {
			this.errors = new ArrayList<>();
		}
		this.errors.add(new ValidationError(field, message));
	}

	@Data
	@RequiredArgsConstructor
	private static class ValidationError {
		private final String field;
		private final String message;
	}
}
