package ru.practicum.ewm.dto;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record EventShortDto(
		String annotation,
		CategoryDto category,
		long confirmedRequests,
		LocalDateTime eventDate,
		long id,
		UserShortDto initiator,
		boolean paid,
		String title,
		long views
) {
}
