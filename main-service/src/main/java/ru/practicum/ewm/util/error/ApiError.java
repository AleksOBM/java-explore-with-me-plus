package ru.practicum.ewm.util.error;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApiError {
	HttpStatus status;
	String reason;
	String message;
	String errors;
	LocalDateTime timestamp = LocalDateTime.now();

	public ApiError(HttpStatus status, String reason, String message, String stackTrace) {
		this.status = status;
		this.reason = reason;
		this.message = message;
		this.errors = stackTrace;
	}
}

