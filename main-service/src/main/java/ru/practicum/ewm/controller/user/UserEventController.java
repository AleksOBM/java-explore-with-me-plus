package ru.practicum.ewm.controller.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
	@ResponseStatus(HttpStatus.CREATED)
	public EventFullDto addEvent(@PathVariable @Positive Long userId,
								 @RequestBody @Valid NewEventDto newEventDto) {

		return eventService.userAddNewEvent(userId, newEventDto);
	}
}
