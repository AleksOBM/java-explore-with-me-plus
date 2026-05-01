package ru.practicum.ewm.dto.category;

import lombok.Builder;

@Builder
public record CategoryDto(
		Long id,
		String name
) {
}
