package ru.practicum.ewm.dto.free;

import lombok.Builder;

import java.util.List;

@Builder
public record FreeGetEventDto(
		String text,
		List<Integer> categories,
		Boolean paid,
		String rangeStart,
		String rangeEnd,
		Boolean onlyAvailable,
		FreeEventSort sort,
		Integer from,
		Integer size
) {

	public enum FreeEventSort {
		EVENT_DATE, VIEWS
	}
}
