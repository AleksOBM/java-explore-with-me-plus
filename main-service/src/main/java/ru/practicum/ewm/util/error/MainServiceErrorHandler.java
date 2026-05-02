package ru.practicum.ewm.util.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.ewm.util.error.exception.ConflictException;
import ru.practicum.ewm.util.error.exception.HitRequestException;
import ru.practicum.ewm.util.error.exception.NotFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class MainServiceErrorHandler {

	/// 500
	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiError handleHitRequestException(HitRequestException e) {
		log.info("500 {}", e.getMessage(), e);
		String stackTrace = getStackTrace(e);
		return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error ....", e.getMessage(), stackTrace);
	}

	/// 400
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidation(@NonNull MethodArgumentNotValidException ex) {
		log.warn("400 Bad Request (validation): {}", ex.getMessage());
		ApiError error = new ApiError(
				HttpStatus.BAD_REQUEST,
				"Incorrectly made request.",
				"Field: " + Objects.requireNonNull(ex.getBindingResult().getFieldError()).getField()
						+ ". Error: " + ex.getBindingResult().getFieldError().getDefaultMessage(),
				getStackTrace(ex)
		);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	/// 400
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiError> handleBadRequest(@NonNull IllegalArgumentException ex) {
		log.warn("400 Bad Request: {}", ex.getMessage());
		ApiError error = new ApiError(
				HttpStatus.BAD_REQUEST,
				"Incorrectly made request.",
				ex.getMessage(),
				getStackTrace(ex)
		);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	/// 404
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiError> handleNotFound(@NonNull NotFoundException ex) {
		log.warn("404 Not Found: {}", ex.getMessage());
		ApiError error = new ApiError(
				HttpStatus.NOT_FOUND,
				"The required object was not found.",
				ex.getMessage(),
				getStackTrace(ex)
		);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	/// 409
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ApiError> handleConflict(@NonNull ConflictException ex) {
		log.warn("409 Conflict: {}", ex.getMessage());
		ApiError error = new ApiError(
				HttpStatus.CONFLICT,
				"For the requested operation the conditions are not met.",
				ex.getMessage(),
				getStackTrace(ex)
		);
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	private String getStackTrace(@NonNull Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
}
