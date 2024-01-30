package com.mateus.demo.exceptions;

import com.fasterxml.jackson.databind.util.ExceptionUtil;
import com.mateus.demo.service.exceptions.DataBindingViolationException;
import com.mateus.demo.service.exceptions.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Value("${server.error.include-exception}")
	private boolean printStackTrace;


	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	protected ResponseEntity<Object> handleMethodArgumentNotValid (
			MethodArgumentNotValidException methodArgumentNotValidException,
			HttpHeaders httpHeaders,
			HttpStatus httpStatus,
			WebRequest webRequest) {
		ErrorResponse errorResponse = new ErrorResponse(
				HttpStatus.UNPROCESSABLE_ENTITY.value(),
				"Validation error. Check errors field"
				);
				for (FieldError fieldError: methodArgumentNotValidException.getBindingResult().getFieldErrors()){
					errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
				}
				return ResponseEntity.unprocessableEntity().body(errorResponse);

	}


	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest webRequest){
		log.error("unknow error occurred", exception);
		return buildErrorResponse (exception, "unknow error occurred", HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
	}

	private ResponseEntity<Object> buildErrorResponse(Exception exception, String message, HttpStatus status, WebRequest request){
		ErrorResponse errorResponse = new ErrorResponse(status.value(),message);
		if (this.printStackTrace){
			errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
		}
		return ResponseEntity.status(status).body(errorResponse);
	}


	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public  ResponseEntity<Object> handleDataIntegrityViolationException(
			DataIntegrityViolationException dataIntegrityViolationException,
			WebRequest webRequest
	) {
		String errorMessage = dataIntegrityViolationException.getMostSpecificCause().getMessage();
		log.error("failed to save entity: "+ errorMessage, dataIntegrityViolationException);
		return buildErrorResponse(
				dataIntegrityViolationException,
				errorMessage,
				HttpStatus.CONFLICT,
				webRequest
		);
	}

	@ExceptionHandler(ObjectNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> handleObjectNotFoundException(
			ObjectNotFoundException objectNotFoundException,
			WebRequest webRequest
	){
		String errorMessage = objectNotFoundException.getMessage();
		log.error("class not found: "+ errorMessage+objectNotFoundException);

		return buildErrorResponse(
				objectNotFoundException,
				errorMessage,
				HttpStatus.NOT_FOUND,
				webRequest
		);
	}

	@ExceptionHandler(DataBindingViolationException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> handleDataBindingViolationException(
			DataBindingViolationException dataBindingViolationException,
			WebRequest webRequest
	){
		String errorMessage = dataBindingViolationException.getMessage();
		log.error("Failed to save entity with associated data"+ errorMessage+dataBindingViolationException);

		return buildErrorResponse(
				dataBindingViolationException,
				errorMessage,
				HttpStatus.CONFLICT,
				webRequest
		);
	}
}
