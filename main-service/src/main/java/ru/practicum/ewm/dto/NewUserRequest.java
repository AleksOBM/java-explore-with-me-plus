package ru.practicum.ewm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record NewUserRequest(

		@NotBlank @Email
		String email,

		@NotBlank
		String name
) {
}
