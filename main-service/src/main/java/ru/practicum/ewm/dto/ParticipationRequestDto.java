package ru.practicum.ewm.dto;

import lombok.Builder;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.ParticipationStatus;

@Builder
public record ParticipationRequestDto(
		Long id,
		String created,
		Long event,
		Long requester,
		ParticipationStatus status
) {
}
