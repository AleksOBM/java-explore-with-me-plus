package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dao.EventRepository;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.mapper.StateMapper;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.util.UtilService;
import ru.practicum.ewm.util.error.exception.BadRequestException;
import ru.practicum.ewm.util.error.exception.ConflictException;
import ru.practicum.ewm.util.error.exception.NotFoundException;
import ru.practicum.ewm.util.specification.EventSpecifications;
import ru.practicum.ewm.util.specification.SpecBuilder;
import ru.practicum.ewm.util.statistic.StatService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

	private final EventRepository eventRepository;
	private final UtilService utilService;
	private final StatService statService;
	private final UserService userService;
	private final CategoryService categoryService;

	@Override
	public List<EventShortDto> getFreeEvents(@NonNull FreeGetDto dto, HttpServletRequest request) {
		statService.sendHitRequest(request);

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
		statService.sendHitRequest(request);
		Event event = utilService.getEventById(eventId);
		return EventMapper.toEventFullDto(event);
	}

	@Override
	public EventFullDto userAddNewEvent(Long userId, @NonNull NewEventDto newEventDto) {
		User initiator = utilService.getUserById(userId);
		Category category = utilService.getCategoryById(newEventDto.category());

		// это заглушка
		Event event = EventMapper.toEntity(
				newEventDto,
				category,
				0,
				LocalDateTime.now().minusHours(1),
				initiator,
				LocalDateTime.now().minusMinutes(1),
				EventState.PUBLISHED,
				10L
		);

		return EventMapper.toEventFullDto(eventRepository.save(event));
	}

	@Override
	public List<EventShortDto> findByUserId(Long userId, Integer from, Integer size) {
		userService.throwIfUserNotFound(userId);

		return eventRepository.findByInitiatorId(userId, PageRequest.of(from / size, size)).stream()
				.map(EventMapper::toEventShortDto)
				.toList();
	}

	@Override
	public EventFullDto findEventById(Long userId, Long eventId) {
		userService.throwIfUserNotFound(userId);

		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new NotFoundException("Ивент с id = " + eventId + " не найден"));

		if (!event.getInitiator().getId().equals(userId)) {
			throw new ConflictException("Пользователь должен быть инициатором");
		}

		return EventMapper.toEventFullDto(event);
	}

	@Override
	public EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
		userService.throwIfUserNotFound(userId);
		return patchEvent(eventId, request, 2, false);
	}

	private EventFullDto patchEvent(Long eventId, UpdateEventUserRequest request, long hoursBeforeStart,
									boolean isAdmin) {
		try {
			if (request.eventDate() != null) {
				Instant deadline = Instant.now().plus(hoursBeforeStart, ChronoUnit.HOURS);
				Instant eventDate = Instant.from(request.eventDate());

				if (!eventDate.isAfter(deadline)) {
					throw new ConflictException("Дата ивента не может быть раньше " + hoursBeforeStart + " часов назад");

				}
			}

			Event event = eventRepository.findById(eventId)
					.orElseThrow(() -> new NotFoundException("Ивент с id = " + eventId + " не найден"));
			StateAction action = request.stateAction();

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
				Category category = categoryService.findEntityById(request.category());
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


}
