package ru.practicum.ewm.service.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dao.CategoryRepository;
import ru.practicum.ewm.dao.EventRepository;
import ru.practicum.ewm.dao.UserRepository;
import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.mapper.StateMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.enums.AdminStateAction;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.model.enums.UserStateAction;
import ru.practicum.ewm.util.error.exception.ConflictException;
import ru.practicum.ewm.util.error.exception.NotFoundException;
import ru.practicum.ewm.util.specification.EventSpecifications;
import ru.practicum.ewm.util.specification.SpecBuilder;
import ru.practicum.ewm.util.statistic.StatRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventServiceImpl implements EventService {

	StatRepository statRepository;
	EventRepository eventRepository;
	UserRepository userRepository;
	CategoryRepository categoryRepository;

	@Override
	public List<EventShortDto> getFreeEvents(@NonNull FreeGetDto dto, HttpServletRequest request) {
		statRepository.sendHitRequest(request);

		SpecBuilder<Event> builder = SpecBuilder.<Event>builder()
				.and(EventSpecifications.isPublished())
				.andIf(dto.text() != null && !dto.text().isBlank(),
						() -> EventSpecifications.textContains(dto.text()))
				.andIf(dto.categories() != null && !dto.categories().isEmpty(),
						() -> EventSpecifications.hasCategories(dto.categories()))
				.andIf(dto.paid() != null,
						() -> EventSpecifications.isPaid(dto.paid()))
				.andIf(Boolean.TRUE.equals(dto.onlyAvailable()),
						() -> EventSpecifications.onlyAvailable(true));

		boolean hasStart = dto.rangeStart() != null;
		boolean hasEnd = dto.rangeEnd() != null;

		if (!hasStart && !hasEnd) {
			builder.and(EventSpecifications.eventDateAfterNow(LocalDateTime.now()));
		} else {
			builder
					.andIf(hasStart,
							() -> EventSpecifications.dateAfter(dto.rangeStart()))
					.andIf(hasEnd,
							() -> EventSpecifications.dateBefore(dto.rangeEnd()));
		}

		Specification<Event> spec = builder.build();

		Sort sort = Sort.unsorted();

		if (dto.sort() != null) {
			switch (dto.sort()) {
				case EVENT_DATE -> sort = Sort.by("eventDate").ascending();
				case VIEWS -> sort = Sort.by("views").descending();
			}
		}

		Pageable pageable = PageRequest.of(
				dto.from() / dto.size(),
				dto.size(),
				sort
		);

		return eventRepository.findAll(spec, pageable)
				.stream()
				.map(EventMapper::toEventShortDto)
				.toList();
	}

	@Override
	public EventFullDto getFreeEventById(Long eventId, HttpServletRequest request) {
		statRepository.sendHitRequest(request);
		Event event = getEventById(eventId);
		return EventMapper.toEventFullDto(event);
	}

	@Override
	public EventFullDto userAddNewEvent(Long userId, @NonNull NewEventDto newEventDto) {
		User initiator = getUserById(userId);
		Category category = getCategoryById(newEventDto.category());

		Event event = EventMapper.toEntity(
				newEventDto,
				category,
				0,
				LocalDateTime.now(),
				initiator,
				null,
				EventState.PENDING,
				0L
		);

		return EventMapper.toEventFullDto(eventRepository.save(event));
	}

	@Override
	public List<EventFullDto> adminGetEvents(AdminGetDto dto) {
		Specification<Event> spec = SpecBuilder.<Event>builder()
				.andIf(dto.users() != null && !dto.users().isEmpty(),
						() -> EventSpecifications.hasUsers(dto.users()))
				.andIf(dto.states() != null && !dto.states().isEmpty(),
						() -> EventSpecifications.hasStates(dto.states()))
				.andIf(dto.categories() != null && !dto.categories().isEmpty(),
						() -> EventSpecifications.hasCategories(dto.categories()))
				.andIf(dto.rangeStart() != null,
						() -> EventSpecifications.dateAfter(dto.rangeStart()))
				.andIf(dto.rangeEnd() != null,
						() -> EventSpecifications.dateBefore(dto.rangeEnd()))
				.build();

		Pageable pageable = PageRequest.of(
				dto.from() / dto.size(),
				dto.size()
		);

		return eventRepository.findAll(spec, pageable)
				.stream()
				.map(EventMapper::toEventFullDto)
				.toList();
	}

	@Override
	public EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest request) {
		Event oldEvent = getEventById(eventId);

		Event newEvent = EventMapper.update(
				oldEvent,
				request,
				request.stateAction().equals(AdminStateAction.REJECT_EVENT) ?
						EventState.CANCELED : EventState.PUBLISHED,
				request.stateAction().equals(AdminStateAction.REJECT_EVENT) ? null : LocalDateTime.now(),
				request.category() == null ?
						Optional.empty() :
						Optional.of(getCategoryById(request.category()))
		);

		return EventMapper.toEventFullDto(newEvent);
	}

	@Override
	public List<EventShortDto> findByUserId(Long userId, Integer from, Integer size) {
		checkUser(userId);

		return eventRepository.findByInitiatorId(userId, PageRequest.of(from / size, size)).stream()
				.map(EventMapper::toEventShortDto)
				.toList();
	}

	@Override
	public EventFullDto findEventById(Long userId, Long eventId) {
		checkUser(userId);

		Event event = getEventById(eventId);

		if (!event.getInitiator().getId().equals(userId)) {
			throw new ConflictException("Пользователь должен быть инициатором");
		}

		return EventMapper.toEventFullDto(event);
	}

	@Override
	public EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
		checkUser(userId);
		return patchEvent(eventId, request, 2, false);
	}

	private EventFullDto patchEvent(Long eventId, UpdateEventUserRequest request, long hoursBeforeStart,
	                                boolean isAdmin) {
		try {
			if (request.eventDate() != null) {
				Instant deadline = Instant.now().plus(hoursBeforeStart, ChronoUnit.HOURS);
				Instant eventDate = Instant.from(request.eventDate());

				if (!eventDate.isAfter(deadline)) {
					throw new ConflictException(
							"Дата ивента не может быть раньше " + hoursBeforeStart + " часов назад"
					);
				}
			}

			Event event = getEventById(eventId);
			UserStateAction action = request.stateAction();

			if (action != null) {
				EventState newState = isAdmin
						? StateMapper.mapAdminEventAction(action)
						: StateMapper.mapUserEventAction(action);

				if (EventState.PUBLISHED.equals(newState)) {
					event.setPublishedOn(LocalDateTime.from(Instant.now()));
				}
				if (newState != null) {
					event.setState(newState);
				}
			}

			if (request.category() != null) {
				Category category = getCategoryById(request.category());
				event.setCategory(category);
			}

			EventMapper.merge(event, request);

			Event patched = eventRepository.save(event);

			log.info("Ивент обновлен: {}", patched.getId());
			return EventMapper.toEventFullDto(patched);

		} catch (DataIntegrityViolationException e) {
			log.debug("Конфликт вовремя обновления ивента {}", request, e);
			throw new ConflictException("Конфликт с другим ивентом");
		}
	}

	private void checkUser(Long userId) {
		if (!userRepository.existsById(userId)) {
			throw new NotFoundException("Пользователь с id=" + userId + " не найден");
		}
	}

	@NonNull
	private User getUserById(long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
	}

	@NonNull
	private Event getEventById(long eventId) {
		return eventRepository.findById(eventId).orElseThrow(
				() -> new NotFoundException("Событие с id=" + eventId + " не найдено")
		);
	}

	@NonNull
	private Category getCategoryById(long categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(
				() -> new NotFoundException("Категория с id=" + categoryId + " не найдена")
		);
	}
}
