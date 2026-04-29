package ru.practicum.ewm.controller.free;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.FreeGetDto;
import ru.practicum.ewm.service.EventService;

import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class FreeEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getFreeEvents(@RequestParam(required = false) String text,
                                             @RequestParam(required = false) List<Integer> categories,
                                             @RequestParam(required = false) Boolean paid,
                                             @RequestParam(required = false) String rangeStart,
                                             @RequestParam(required = false) String rangeEnd,
                                             @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                             @RequestParam(required = false) FreeGetDto.FreeEventSort sort,
                                             @RequestParam(required = false, defaultValue = "0") Integer from,
                                             @RequestParam(required = false, defaultValue = "10") Integer size,
                                             HttpServletRequest request) {

        FreeGetDto freeGetDto = FreeGetDto.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();

        return eventService.getFreeEvents(freeGetDto, request);
    }

    @GetMapping(value = "/{eventId}")
    public EventFullDto getFreeEventById(@PathVariable Long eventId,
                                         HttpServletRequest request) {
        return eventService.getFreeEventById(eventId, request);
    }
}
