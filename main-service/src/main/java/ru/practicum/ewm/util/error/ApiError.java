package ru.practicum.ewm.util.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Builder
@Getter
public class ApiError {
	private final HttpStatus status;
	private final String reason;
	private final String message;
	private final String errors;
	private final LocalDateTime timestamp = LocalDateTime.now();

	public ApiError(HttpStatus status, String reason, String message, String stackTrace) {
		this.status = status;
		this.reason = reason;
		this.message = message;
		this.errors = stackTrace;
	}
}

