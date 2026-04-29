package ru.practicum.ewm.util;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dao.CategoryRepository;
import ru.practicum.ewm.dao.EventRepository;
import ru.practicum.ewm.dao.UserRepository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.util.error.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UtilService {

	private final UserRepository userRepository;
	private final EventRepository eventRepository;
	private final CategoryRepository categoryRepository;

	@NonNull
	public User getUserById(long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
	}

	@NonNull
	public Event getEventById(long eventId) {
		return eventRepository.findById(eventId).orElseThrow(
				() -> new NotFoundException("Событие с id=" + eventId + " не найдено")
		);
	}

	public Category getCategoryById(long categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(
				() -> new NotFoundException("Категория с id=" + categoryId + " не найдена")
		);
	}

	public static String formatDateTime(LocalDateTime dateTime) {
		if (dateTime == null) return null;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
		return dateTime.format(formatter);
	}

	public static LocalDateTime parseDateTime(String dateTimeStr) {
		if (dateTimeStr == null || dateTimeStr.isBlank()) return null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
		return LocalDateTime.parse(dateTimeStr, formatter);
	}
}
