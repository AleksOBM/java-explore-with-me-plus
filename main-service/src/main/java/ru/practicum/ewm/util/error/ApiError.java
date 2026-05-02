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

	/// Код статуса HTTP-ответа
	HttpStatus status;

	/// Общее описание причины ошибки
	String reason;

	/// Сообщение об ошибке
	String message;

	/// Список стектрейсов или описания ошибок
	String errors;

	/// Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss")
	LocalDateTime timestamp = LocalDateTime.now();

	public ApiError(HttpStatus status, String reason, String message, String stackTrace) {
		this.status = status;
		this.reason = reason;
		this.message = message;
		this.errors = stackTrace;
	}
}

