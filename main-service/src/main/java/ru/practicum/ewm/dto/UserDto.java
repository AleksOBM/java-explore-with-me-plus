package ru.practicum.ewm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@NotNull
@Builder
public record UserDto(

		Long id,

		@NotBlank
		String name,

		@Email
		@NotBlank
		String email
) {
}
