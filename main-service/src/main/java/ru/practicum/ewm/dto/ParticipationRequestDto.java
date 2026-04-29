package ru.practicum.ewm.dto;

import lombok.Builder;
import ru.practicum.ewm.model.EventState;

@Builder
public record ParticipationRequestDto(
		Long id,
		String created,
		Long event,
		Long requester,
		EventState status
) {
}
