package ru.practicum.ewm.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.service.EventService;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class UserEventController {

	private final EventService eventService;

	@PostMapping
	public EventFullDto addEvent(@PathVariable Long userId,
	                             @RequestBody @Valid NewEventDto newEventDto) {

		return eventService.userAddNewEvent(userId, newEventDto);
	}
}
