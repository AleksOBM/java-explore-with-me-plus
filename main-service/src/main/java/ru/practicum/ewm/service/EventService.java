package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.EventBigDto;
import ru.practicum.ewm.dto.EventDto;
import ru.practicum.ewm.dto.EventLowDto;
import ru.practicum.ewm.dto.FreeGetDto;

import java.util.List;

@Transactional
public interface EventService {

	@Transactional(readOnly = true)
	List<EventDto> getFreeEvents(FreeGetDto freeGetDto);

	EventDto getFreeEventById(Long eventId, HttpServletRequest request);

	EventBigDto userAddNewEvent(Long userId, EventLowDto eventLowDto);
}
