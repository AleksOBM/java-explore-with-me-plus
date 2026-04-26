package ru.practicum.ewm.dto.free;

import lombok.Builder;

@Builder
public record EventLocationDto(
		String lat,
		String lon
) {
}
