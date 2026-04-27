package ru.practicum.ewm.dto;

import lombok.Builder;

@Builder
public record ParticipationRequestDto(
		Long id,
		String created,
		Long event,
		Long requester,
		String status
) {
}
