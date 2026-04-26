package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record EventCategoryDto(

		Long id,

		@NotBlank
		String name
) {
}
