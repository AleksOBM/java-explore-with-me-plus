package ru.practicum.ewm.model;

import lombok.Builder;

@Builder
public record Location(
		Float lat,
		Float lon
) {
}
