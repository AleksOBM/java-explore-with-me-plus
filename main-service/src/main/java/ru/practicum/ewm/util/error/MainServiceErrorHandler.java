package ru.practicum.ewm.util.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.util.error.exception.HitRequestException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class MainServiceErrorHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiError handleHitRequestException(HitRequestException e) {
		log.info("500 {}", e.getMessage(), e);
		String stackTrace = getStackTrace(e);
		return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error ....", e.getMessage(), stackTrace);
	}

	private String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiError handleNotFoundException(final ru.practicum.ewm.util.error.exception.NotFoundException e) {
		log.info("404 {}", e.getMessage());
		return new ApiError(
				HttpStatus.NOT_FOUND,
				"Запрашиваемый объект не найден",
				e.getMessage(),
				getStackTrace(e)
		);
	}
}
