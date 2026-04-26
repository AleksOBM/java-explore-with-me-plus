package ru.practicum.ewm.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;
import ru.practicum.ewm.dto.free.FreeEventDto;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

	public FreeEventDto toFreeEventDto(@NonNull Event event) {
		return FreeEventDto.builder()
				.id(event.getId())
				.annotation(event.getAnnotation())
				.category(event.getCategory())
				.description(event.getDescription())
				.eventDate(event.getEventDate())
				.location(LocationMapper.toDto(event.getLocation()))
				.paid(event.isPaid())
				.participantLimit(event.getParticipantLimit())
				.requestModeration(event.isRequestModeration())
				.title(event.getTitle())
				.build();
	}

	public Event fromFreeEventDto(@NonNull FreeEventDto freeEventDto,
	                              int confirmedRequests,
	                              LocalDateTime createdOn,
	                              User initiator,
	                              LocalDateTime publishedOn,
	                              EventState state,
	                              long views) {
		return Event.builder()
				.id(freeEventDto.id())
				.annotation(freeEventDto.annotation())
				.category(freeEventDto.category())
				.confirmedRequests(confirmedRequests)
				.createdOn(createdOn)
				.description(freeEventDto.description())
				.eventDate(freeEventDto.eventDate())
				.initiator(initiator)
				.location(LocationMapper.fromDto(freeEventDto.location()))
				.paid(freeEventDto.paid())
				.participantLimit(freeEventDto.participantLimit())
				.publishedOn(publishedOn)
				.requestModeration(freeEventDto.requestModeration())
				.state(state)
				.title(freeEventDto.title())
				.views(views)
				.build();
	}
}
