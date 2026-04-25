package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.free.FreeEventDto;
import ru.practicum.ewm.dto.free.FreeGetEventDto;

import java.util.List;

public interface EventService {

	@Transactional(readOnly = true)
	List<FreeEventDto> getFreeEvents(FreeGetEventDto freeGetEventDto);

	@Transactional(readOnly = true)
	FreeEventDto getFreeEventById(Long eventId, HttpServletRequest request);
}
