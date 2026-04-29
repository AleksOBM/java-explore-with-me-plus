package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dao.EventRepository;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.util.UtilService;
import ru.practicum.ewm.util.specification.EventSpecifications;
import ru.practicum.ewm.util.specification.SpecBuilder;
import ru.practicum.ewm.util.statistic.StatService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

	private final EventRepository eventRepository;
	private final UtilService utilService;
	private final StatService statService;

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
	public List<EventFullDto> adminGetEvents(AdminGetDto adminGetDto) {
		return List.of();
	}

	@Override
	public EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest request) {
		Event oldEvent = utilService.getEventById(eventId);

		Event newEvent = EventMapper.update(
				oldEvent,
				request,
				request.stateAction().equals(AdminStateAction.REJECT_EVENT) ?
						EventState.CANCELED : EventState.PUBLISHED,
				request.stateAction().equals(AdminStateAction.REJECT_EVENT) ? null : LocalDateTime.now(),
				request.category() == null ?
						Optional.empty() :
						Optional.of(utilService.getCategoryById(request.category()))
		);

		return EventMapper.toEventFullDto(newEvent);
	}
}
