package ru.practicum.ewm.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

@UtilityClass
public class EventMapper {

	public EventShortDto toEventShortDto(@NonNull Event event) {
		return EventShortDto.builder()
				.annotation(event.getAnnotation())
				.category(CategoryMapper.toDto(event.getCategory()))
				.confirmedRequests(event.getConfirmedRequests())
				.eventDate(event.getEventDate())
				.id(event.getId())
				.initiator(UserMapper.toUserShortDto(event.getInitiator()))
				.paid(event.isPaid())
				.title(event.getTitle())
				.views(event.getViews())
				.build();
	}

	public EventFullDto toEventFullDto(@NonNull Event event) {
		return EventFullDto.builder()
				.id(event.getId())
				.annotation(event.getAnnotation())
				.category(CategoryMapper.toDto(event.getCategory()))
				.confirmedRequests(event.getConfirmedRequests())
				.createdOn(event.getCreatedOn())
				.description(event.getDescription())
				.eventDate(event.getEventDate())
				.initiator(UserMapper.toUserShortDto(event.getInitiator()))
				.location(event.getLocation())
				.paid(event.isPaid())
				.participantLimit(event.getParticipantLimit())
				.publishedOn(event.getPublishedOn())
				.requestModeration(event.isRequestModeration())
				.state(event.getState())
				.title(event.getTitle())
				.views(event.getViews())
				.build();
	}

	public Event toEntity(@NonNull NewEventDto newEventDto,
	                      Category category,
	                      long confirmedRequests,
	                      LocalDateTime createdOn,
	                      User initiator,
	                      LocalDateTime publishedOn,
	                      EventState state,
	                      long views) {
		return Event.builder()
				.annotation(newEventDto.annotation())
				.category(category)
				.confirmedRequests(confirmedRequests)
				.createdOn(createdOn)
				.description(newEventDto.description())
				.eventDate(newEventDto.eventDate())
				.initiator(initiator)
				.location(newEventDto.location())
				.paid(newEventDto.paid())
				.participantLimit(newEventDto.participantLimit())
				.publishedOn(publishedOn)
				.requestModeration(newEventDto.requestModeration())
				.state(state)
				.title(newEventDto.title())
				.views(views)
				.build();
	}

	public Event toEntity(@NonNull EventFullDto eventFullDto, LocalDateTime createdOn) {
		return Event.builder()
				.id(eventFullDto.id())
				.annotation(eventFullDto.annotation())
				.category(CategoryMapper.toEntity(eventFullDto.category()))
				.confirmedRequests(eventFullDto.confirmedRequests())
				.createdOn(createdOn)
				.description(eventFullDto.description())
				.eventDate(eventFullDto.eventDate())
				.initiator(UserMapper.toEntity(eventFullDto.initiator()))
				.location(eventFullDto.location())
				.paid(eventFullDto.paid())
				.participantLimit(eventFullDto.participantLimit())
				.publishedOn(eventFullDto.publishedOn())
				.requestModeration(eventFullDto.requestModeration())
				.state(eventFullDto.state())
				.title(eventFullDto.title())
				.views(eventFullDto.views())
				.build();
	}

	public static Event update(Event oldEvent,
	                           UpdateEventAdminRequest request,
	                           EventState state,
	                           LocalDateTime publishedOn,
	                           Optional<Category> category) {

		return oldEvent.toBuilder()
				.annotation(request.annotation() != null ?
						request.annotation() : oldEvent.getAnnotation()
				)
				.category(category.orElse(oldEvent.getCategory()))
				.description(request.description() != null ?
						request.description() : oldEvent.getDescription()
				)
				.eventDate(request.eventDate() != null ?
						request.eventDate() : oldEvent.getEventDate()
				)
				.location(request.location() != null ?
						request.location() : oldEvent.getLocation()
				)
				.paid(request.paid() != null ?
						request.paid() : oldEvent.isPaid()
				)
				.participantLimit(request.participantLimit() != null ?
						request.participantLimit() : oldEvent.getParticipantLimit()
				)
				.requestModeration(request.requestModeration() != null ?
						request.requestModeration() : oldEvent.isRequestModeration()
				)
				.state(state)
				.title(request.title() != null ?
						request.title() : oldEvent.getTitle()
				)
				.publishedOn(publishedOn)
				.build();
	}
}
