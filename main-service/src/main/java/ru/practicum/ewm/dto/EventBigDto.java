package ru.practicum.ewm.dto;

import lombok.Builder;
import ru.practicum.ewm.model.EventState;

import java.time.LocalDateTime;

@Builder
public record EventBigDto(
		Long id,
		String annotation,
		EventCategoryDto category,
		int confirmedRequests,
		LocalDateTime createdOn,
		String description,
		LocalDateTime eventDate,
		UserDto initiator,
		EventLocationDto location,
		boolean paid,
		int participantLimit,
		LocalDateTime publishedOn,
		boolean requestModeration,
		EventState state,
		String title,
		long views
) {
}