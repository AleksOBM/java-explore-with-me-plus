package ru.practicum.ewm.dto;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record EventDto(
		Long id,
		String annotation,
		EventCategoryDto category,
		String description,
		LocalDateTime eventDate,
		EventLocationDto location,
		boolean paid,
		int participantLimit,
		boolean requestModeration,
		String title
) {
}
