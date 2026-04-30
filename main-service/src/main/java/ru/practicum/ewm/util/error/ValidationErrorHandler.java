package ru.practicum.ewm.util.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.util.error.exception.NotFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class ValidationErrorHandler {

	// 400
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CustomApiError> handleValidation(@NonNull MethodArgumentNotValidException ex) {
		log.warn("400 Bad Request (validation): {}", ex.getMessage());
		CustomApiError error = new CustomApiError(
				HttpStatus.BAD_REQUEST.name(),
				"Incorrectly made request.",
				"Field: " + ex.getBindingResult().getFieldError().getField()
						+ ". Error: " + ex.getBindingResult().getFieldError().getDefaultMessage(),
				getStackTrace(ex)
		);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	// 404
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<CustomApiError> handleNotFound(@NonNull NotFoundException ex) {
		log.warn("404 Not Found: {}", ex.getMessage());
		CustomApiError error = new CustomApiError(
				HttpStatus.NOT_FOUND.name(),
				"The required object was not found.",
				ex.getMessage(),
				getStackTrace(ex)
		);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	// 400
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<CustomApiError> handleBadRequest(@NonNull IllegalArgumentException ex) {
		log.warn("400 Bad Request: {}", ex.getMessage());
		CustomApiError error = new CustomApiError(
				HttpStatus.BAD_REQUEST.name(),
				"Incorrectly made request.",
				ex.getMessage(),
				getStackTrace(ex)
		);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	private String getStackTrace(@NonNull Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
}
