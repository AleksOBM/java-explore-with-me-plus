package ru.practicum.ewm.dto;

import lombok.Builder;

@Builder
public record UserDto(
		String email,
		Long id,
		String name
) {
}
