package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.User;

import static ru.practicum.ewm.util.Utils.*;

@Component
public class RequestMapper {

	public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
		if (participationRequest == null) return null;
		return ParticipationRequestDto.builder()
				.event(participationRequest.getEvent().getId())
				.requester(participationRequest.getRequester().getId())
				.status(participationRequest.getStatus())
				.created(formatDateTime(participationRequest.getCreated()))
				.id(participationRequest.getId())
				.build();
	}

	public static ParticipationRequest toEntity(ParticipationRequestDto participationRequestDto, Event event,
	                                            User requester) {
		if (participationRequestDto == null) return null;
		return ParticipationRequest.builder()
				.id(participationRequestDto.id())
				.event(event)
				.requester(requester)
				.status(participationRequestDto.status())
				.created(parseDateTime(participationRequestDto.created()))
				.build();
	}
}
