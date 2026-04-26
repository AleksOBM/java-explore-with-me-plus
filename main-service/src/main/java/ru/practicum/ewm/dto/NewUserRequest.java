package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record NewUserRequest(

		@NotBlank
		String email,

		@NotBlank
		String name
) {
}
