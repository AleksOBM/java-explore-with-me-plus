package ru.practicum.ewm.dto.free;

import lombok.Builder;
import ru.practicum.ewm.model.EventCategory;

import java.time.LocalDateTime;

@Builder
public record FreeEventDto(
		long id,
		String annotation,
		EventCategory category,
		String description,
		LocalDateTime eventDate,
		EventLocationDto location,
		boolean paid,
		int participantLimit,
		boolean requestModeration,
		String title
) {
}
