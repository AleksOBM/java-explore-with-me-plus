package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dao.EventRepository;
import ru.practicum.ewm.dto.EventBigDto;
import ru.practicum.ewm.dto.EventDto;
import ru.practicum.ewm.dto.EventLowDto;
import ru.practicum.ewm.dto.FreeGetDto;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventCategory;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.util.UtilService;
import ru.practicum.ewm.util.error.exception.HitRequestException;
import ru.practicum.ewm.util.specification.EventSpecifications;
import ru.practicum.ewm.util.specification.SpecBuilder;
import ru.practicum.stat.client.StatClient;
import ru.practicum.stat.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

	private final EventRepository eventRepository;
	private final UtilService utilService;
	private final StatClient statClient;

	@Value("${app.name}")
	private String appName;

	@Override
	public List<EventDto> getFreeEvents(@NonNull FreeGetDto dto) {
		Specification<Event> spec = SpecBuilder.<Event>builder()
				.andIf(dto.text() != null && !dto.text().isBlank(),
						() -> EventSpecifications.textContains(dto.text()))
				.andIf(dto.categories() != null && !dto.categories().isEmpty(),
						() -> EventSpecifications.hasCategories(dto.categories()))
				.andIf(dto.paid() != null,
						() -> EventSpecifications.isPaid(dto.paid()))
				.andIf(dto.rangeStart() != null,
						() -> EventSpecifications.dateAfter(dto.rangeStart()))
				.andIf(dto.rangeEnd() != null,
						() -> EventSpecifications.dateBefore(dto.rangeEnd()))
				.andIf(Boolean.TRUE.equals(dto.onlyAvailable()),
						() -> EventSpecifications.onlyAvailable(true))
				.build();

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
				.map(EventMapper::toEventDto)
				.toList();
	}

	@Override
	public EventDto getFreeEventById(Long eventId, HttpServletRequest request) {
		sendHitRequest(request);
		Event event = utilService.getEventById(eventId);
		return EventMapper.toEventDto(event);
	}

	@Override
	public EventBigDto userAddNewEvent(Long userId, EventLowDto eventLowDto) {
		User initiator = utilService.getUserById(userId);
		EventCategory eventCategory = utilService.getCategoryById(eventLowDto.category());

		// todo: это заглушка
		Event event = EventMapper.fromEventLowDto(
				eventLowDto,
				eventCategory,
				0,
				LocalDateTime.now(),
				initiator,
				LocalDateTime.now().plusDays(1),
				EventState.PUBLISHED,
				10L
		);

		return EventMapper.toEventBigDto(eventRepository.save(event));
	}

	private void sendHitRequest(HttpServletRequest request) {
		try {
			statClient.hit(EndpointHitDto.builder()
					.ip(request.getRemoteAddr())
					.uri(request.getRequestURI())
					.app(appName)
					.timestamp(LocalDateTime.now())
					.build()
			);
		} catch (Exception ex) {
			throw new HitRequestException(ex);
		}
	}
}
