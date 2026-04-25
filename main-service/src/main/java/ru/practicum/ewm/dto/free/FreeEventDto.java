package ru.practicum.ewm.dto.free;

import lombok.Builder;
import ru.practicum.ewm.model.EventCategory;
import ru.practicum.ewm.model.EventLocation;

import java.time.LocalDateTime;

@Builder
public record FreeEventDto(
		String annotation,
		EventCategory category,
		String description,
		LocalDateTime eventDate,
		EventLocation location,
		boolean paid,
		int participantLimit,
		boolean requestModeration,
		String title
) {
}
