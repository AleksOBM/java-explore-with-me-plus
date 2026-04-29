package ru.practicum.ewm.util.error;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class CustomApiError {
    private final String status;
    private final String reason;
    private final String message;
    private final String errors;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public CustomApiError(String status, String reason, String message, String stackTrace) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.errors = stackTrace;
    }
}
