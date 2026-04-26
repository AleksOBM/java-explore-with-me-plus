package ru.practicum.ewm.dto.free;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@NotNull
public record FreeGetEventBody(

		@NotBlank
		String text,

		@NotNull
		@Size(min = 1)
		List<Integer> categories
) {
}
