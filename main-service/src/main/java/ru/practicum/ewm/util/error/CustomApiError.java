package ru.practicum.ewm.util.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomApiError {
	String status;
	String reason;
	String message;
	String errors;
	LocalDateTime timestamp = LocalDateTime.now();

	public CustomApiError(String status, String reason, String message, String stackTrace) {
		this.status = status;
		this.reason = reason;
		this.message = message;
		this.errors = stackTrace;
	}
}
