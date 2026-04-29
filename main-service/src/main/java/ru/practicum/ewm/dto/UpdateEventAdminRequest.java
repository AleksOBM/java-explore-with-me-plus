package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.model.AdminStateAction;

import java.time.LocalDateTime;

@Builder
public record UpdateEventAdminRequest(

		String annotation,

		Long category,

		@Size(min = 20, max = 7000)
		String description,

		LocalDateTime eventDate,

		Location location,

		Boolean paid,

		Integer participantLimit,

		@NotNull
		Boolean requestModeration,

		@NotNull
		AdminStateAction stateAction,

		@Size(min = 3, max = 120)
		String title
) {
}
