package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.AdminGetDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.service.event.EventService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

	private final EventService eventService;

	/**
	 * Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
	 * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
	 */
	@GetMapping
	public List<EventFullDto> adminGetEvents(
			@RequestParam(required = false) List<Integer> users,
			@RequestParam(required = false) List<EventState> states,
			@RequestParam(required = false) List<Integer> categories,
			@RequestParam(required = false) String rangeStart,
			@RequestParam(required = false) String rangeEnd,
			@RequestParam(required = false, defaultValue = "0") Integer from,
			@RequestParam(required = false, defaultValue = "10") Integer size) {

		AdminGetDto getDto = AdminGetDto.builder()
				.users(users)
				.states(states)
				.categories(categories)
				.rangeStart(rangeStart)
				.rangeEnd(rangeEnd)
				.from(from)
				.size(size)
				.build();

		return eventService.adminGetEvents(getDto);
	}

	@PatchMapping("/{eventId}")
	public EventFullDto adminUpdateEvent(
			@PathVariable Long eventId,
			@RequestBody UpdateEventAdminRequest request) {

		return eventService.adminUpdateEvent(eventId, request);
	}
}
