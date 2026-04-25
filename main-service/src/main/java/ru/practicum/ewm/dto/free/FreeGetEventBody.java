package ru.practicum.ewm.dto.free;

import java.util.List;

public record FreeGetEventBody(
		String text,
		List<Integer> categories
) {
}
