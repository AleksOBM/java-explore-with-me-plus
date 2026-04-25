package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dao.EventRepository;
import ru.practicum.ewm.dto.free.FreeEventDto;
import ru.practicum.ewm.dto.free.FreeGetEventDto;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.util.error.exception.HitRequestException;
import ru.practicum.ewm.util.error.exception.NotFoundException;
import ru.practicum.stat.client.StatClient;
import ru.practicum.stat.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

	private final EventRepository eventRepository;
	private final StatClient statClient;

	@Value("${app.name}")
	private String appName;

	@Override
	public List<FreeEventDto> getFreeEvents(FreeGetEventDto freeGetEventDto) {
		return List.of();
	}

	@Override
	public FreeEventDto getFreeEventById(Long eventId, HttpServletRequest request) {
		sendHitRequest(request);
		Event event = getEventById(eventId);
		return EventMapper.toFreeEventDto(event);
	}

	@NonNull
	private Event getEventById(Long eventId) {
		return eventRepository.findById(eventId).orElseThrow(
				() -> new NotFoundException("Событие с id=" + eventId + " не найдено")
		);
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
