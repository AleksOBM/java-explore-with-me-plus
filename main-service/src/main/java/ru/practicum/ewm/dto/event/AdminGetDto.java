package ru.practicum.ewm.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ru.practicum.ewm.model.enums.EventState;

import java.util.List;

@NotNull
@Builder
public record AdminGetDto(
		List<Integer> users,
		List<EventState> states,
		List<Integer> categories,
		String rangeStart,
		String rangeEnd,
		Integer from,
		Integer size
) {
}