package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import ru.practicum.ewm.model.Location;

import java.time.LocalDateTime;

@Builder
public record NewEventDto(

		@NotBlank
		@Size(min = 20, max = 2000)
		String annotation,

		@NotNull
		Long category,

		@NotBlank
		@Size(min = 20, max = 7000)
		String description,

		@NotNull
		LocalDateTime eventDate,

		@NotNull
		Location location,

		Boolean paid,

		Integer participantLimit,

		Boolean requestModeration,

		@NotBlank
		@Size(min = 3, max = 120)
		String title
) {
}
