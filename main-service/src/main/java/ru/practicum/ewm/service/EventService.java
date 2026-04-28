package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.*;

import java.util.List;

@Transactional
public interface EventService {

	List<EventShortDto> getFreeEvents(FreeGetDto freeGetDto, HttpServletRequest request);

	EventFullDto getFreeEventById(Long eventId, HttpServletRequest request);

	EventFullDto userAddNewEvent(Long userId, NewEventDto newEventDto);

	@Transactional(readOnly = true)
	List<EventFullDto> adminGetEvents(AdminGetDto adminGetDto);

	@Transactional(propagation = Propagation.REQUIRED)
	EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest request);
}
