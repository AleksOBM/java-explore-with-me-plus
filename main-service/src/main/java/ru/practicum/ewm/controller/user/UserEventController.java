package ru.practicum.ewm.controller.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class UserEventController {

	private final EventService eventService;
	private final RequestService requestService;

    @GetMapping
    public List<EventShortDto> findEventsByUserId(@PathVariable @Positive Long userId,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.findByUserId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto findEventById(@PathVariable @Positive Long userId,
                                      @PathVariable @Positive Long eventId) {
        return eventService.findEventById(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable @Positive Long userId,
                                 @RequestBody @Valid NewEventDto newEventDto) {

        return eventService.userAddNewEvent(userId, newEventDto);
    }

	@PatchMapping("/{eventId}")
	public EventFullDto patchEvent(@PathVariable @Positive Long userId,
								   @PathVariable @Positive Long eventId,
								   @RequestBody UpdateEventUserRequest request) {
		return eventService.patchEvent(userId, eventId, request);
	}

	@GetMapping("/{eventId}/requests")
	public List<ParticipationRequestDto> getRequests(@PathVariable @Positive Long userId,
													 @PathVariable @Positive Long eventId) {
		return requestService.findByEventId(userId, eventId);
	}

	@PatchMapping("/{eventId}/requests")
	public EventRequestStatusUpdateResult patchRequests(@PathVariable @Positive Long userId,
														@PathVariable @Positive Long eventId,
														@RequestBody @Valid EventRequestStatusUpdateRequest request) {
		return requestService.updateStatusRequest(userId, eventId, request);
	}
}
