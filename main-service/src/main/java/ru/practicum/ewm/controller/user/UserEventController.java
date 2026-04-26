package ru.practicum.ewm.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.dto.EventBigDto;
import ru.practicum.ewm.dto.EventDto;
import ru.practicum.ewm.service.EventService;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class UserEventController {

	private final EventService eventService;

	@PostMapping
	public EventBigDto addEvent(@PathVariable Long userId,
	                            @RequestBody @Valid EventDto userGetEventDto) {

		return eventService.userAddNewEvent(userId, userGetEventDto);
	}
}
