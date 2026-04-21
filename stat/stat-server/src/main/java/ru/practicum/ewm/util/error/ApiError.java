package ru.practicum.ewm.util.error;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiError {
	private String errors;
    private String message;
	private String reason;
	private HttpStatus status;
	private LocalDateTime timestamp = LocalDateTime.now();

	public ApiError(HttpStatus status, String reason, String message, String stackTrace) {
		this.status = status;
		this.message = message;
		this.reason = reason;
		this.errors = stackTrace;
	}
}

