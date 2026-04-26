package ru.practicum.ewm.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;
import ru.practicum.ewm.dto.EventBigDto;
import ru.practicum.ewm.dto.EventDto;
import ru.practicum.ewm.dto.EventLowDto;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventCategory;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

	public EventDto toEventDto(@NonNull Event event) {
		return EventDto.builder()
				.id(event.getId())
				.annotation(event.getAnnotation())
				.category(CategoryMapper.toDto(event.getCategory()))
				.description(event.getDescription())
				.eventDate(event.getEventDate())
				.location(LocationMapper.toDto(event.getLocation()))
				.paid(event.isPaid())
				.participantLimit(event.getParticipantLimit())
				.requestModeration(event.isRequestModeration())
				.title(event.getTitle())
				.build();
	}

	public EventLowDto toEventLowDto(@NonNull Event event) {
		return EventLowDto.builder()
				.id(event.getId())
				.annotation(event.getAnnotation())
				.category(event.getCategory().getId())
				.description(event.getDescription())
				.eventDate(event.getEventDate())
				.location(LocationMapper.toDto(event.getLocation()))
				.paid(event.isPaid())
				.participantLimit(event.getParticipantLimit())
				.requestModeration(event.isRequestModeration())
				.title(event.getTitle())
				.build();
	}

	public EventBigDto toEventBigDto(@NonNull Event event) {
		return EventBigDto.builder()
				.id(event.getId())
				.annotation(event.getAnnotation())
				.category(CategoryMapper.toDto(event.getCategory()))
				.confirmedRequests(event.getConfirmedRequests())
				.createdOn(event.getCreatedOn())
				.description(event.getDescription())
				.eventDate(event.getEventDate())
				.initiator(UserMapper.toUserDto(event.getInitiator()))
				.location(LocationMapper.toDto(event.getLocation()))
				.paid(event.isPaid())
				.participantLimit(event.getParticipantLimit())
				.publishedOn(event.getPublishedOn())
				.requestModeration(event.isRequestModeration())
				.state(event.getState())
				.title(event.getTitle())
				.views(event.getViews())
				.build();
	}

	public Event fromEventDto(@NonNull EventDto eventDto,
	                          int confirmedRequests,
	                          LocalDateTime createdOn,
	                          User initiator,
	                          LocalDateTime publishedOn,
	                          EventState state,
	                          long views) {
		return Event.builder()
				.id(eventDto.id())
				.annotation(eventDto.annotation())
				.category(CategoryMapper.toEntity(eventDto.category()))
				.confirmedRequests(confirmedRequests)
				.createdOn(createdOn)
				.description(eventDto.description())
				.eventDate(eventDto.eventDate())
				.initiator(initiator)
				.location(LocationMapper.fromDto(eventDto.location()))
				.paid(eventDto.paid())
				.participantLimit(eventDto.participantLimit())
				.publishedOn(publishedOn)
				.requestModeration(eventDto.requestModeration())
				.state(state)
				.title(eventDto.title())
				.views(views)
				.build();
	}

	public Event fromEventLowDto(@NonNull EventLowDto eventLowDto,
							  EventCategory category,
	                          int confirmedRequests,
	                          LocalDateTime createdOn,
	                          User initiator,
	                          LocalDateTime publishedOn,
	                          EventState state,
	                          long views) {
		return Event.builder()
				.id(eventLowDto.id())
				.annotation(eventLowDto.annotation())
				.category(category)
				.confirmedRequests(confirmedRequests)
				.createdOn(createdOn)
				.description(eventLowDto.description())
				.eventDate(eventLowDto.eventDate())
				.initiator(initiator)
				.location(LocationMapper.fromDto(eventLowDto.location()))
				.paid(eventLowDto.paid())
				.participantLimit(eventLowDto.participantLimit())
				.publishedOn(publishedOn)
				.requestModeration(eventLowDto.requestModeration())
				.state(state)
				.title(eventLowDto.title())
				.views(views)
				.build();
	}
}
