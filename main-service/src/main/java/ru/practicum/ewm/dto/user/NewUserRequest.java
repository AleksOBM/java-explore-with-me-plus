package ru.practicum.ewm.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record NewUserRequest(

		@NotBlank
		String name,

		@NotBlank @Email
		String email
) {
}
