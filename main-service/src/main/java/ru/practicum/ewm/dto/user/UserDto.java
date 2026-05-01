package ru.practicum.ewm.dto.user;

import lombok.Builder;

@Builder
public record UserDto(
		String email,
		Long id,
		String name
) {
}
