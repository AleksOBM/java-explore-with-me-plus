package ru.practicum.ewm.service.event;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;

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

	List<EventShortDto> findByUserId(Long userId, Integer from, Integer size);

	EventFullDto findEventById(Long userId, Long eventId);

	EventFullDto patchEvent(Long userId, Long eventId, UpdateEventUserRequest request);
}
