package ru.practicum.ewm.controller.free;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.free.FreeEventDto;
import ru.practicum.ewm.dto.free.FreeGetEventBody;
import ru.practicum.ewm.dto.free.FreeGetEventDto;
import ru.practicum.ewm.service.EventService;

import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class FreeEventController {

	private final EventService eventService;

	/**
	 * {{baseUrl}}/events?
	 * paid=true&
	 * rangeStart=2022-01-06%2013%3A30%3A38&
	 * rangeEnd=2097-09-06%2013%3A30%3A38&
	 * onlyAvailable=false&
	 * sort=EVENT_DATE&
	 * from=0&
	 * size=1000
	 */
	@GetMapping
	public List<FreeEventDto> getFreeEvents(@Valid @RequestBody FreeGetEventBody body,
	                                        @RequestParam Boolean paid,
	                                        @RequestParam String rangeStart,
	                                        @RequestParam String rangeEnd,
	                                        @RequestParam(defaultValue = "false") Boolean onlyAvailable,
	                                        @RequestParam FreeGetEventDto.FreeEventSort sort,
	                                        @RequestParam(defaultValue = "0") Integer from,
	                                        @RequestParam(defaultValue = "10") Integer size) {

		FreeGetEventDto freeGetEventDto = FreeGetEventDto.builder()
				.text(body.text())
				.categories(body.categories())
				.paid(paid)
				.rangeStart(rangeStart)
				.rangeEnd(rangeEnd)
				.onlyAvailable(onlyAvailable)
				.sort(sort)
				.from(from)
				.size(size)
				.build();

		return eventService.getFreeEvents(freeGetEventDto);
	}

	@GetMapping(value = "/{eventId}")
	public FreeEventDto getFreeEventById(@PathVariable Long eventId,
	                                     HttpServletRequest request) {
		return eventService.getFreeEventById(eventId, request);
	}
}
