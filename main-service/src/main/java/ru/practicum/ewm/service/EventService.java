package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.dto.FreeGetDto;

import java.util.List;

@Transactional
public interface EventService {

	List<EventShortDto> getFreeEvents(FreeGetDto freeGetDto, HttpServletRequest request);

	EventFullDto getFreeEventById(Long eventId, HttpServletRequest request);

	EventFullDto userAddNewEvent(Long userId, NewEventDto newEventDto);
}
