package ru.practicum.ewm.dto;

import lombok.Builder;

@Builder
public record EventLocationDto(
		String lat,
		String lon
) {
}
