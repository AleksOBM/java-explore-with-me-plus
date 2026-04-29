package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.Location;

import java.time.LocalDateTime;

@Builder
public record EventFullDto(

        @NotBlank
        String annotation,

        @NotNull
        CategoryDto category,

        Long confirmedRequests,

        LocalDateTime createdOn,

        String description,

        @NotNull
        LocalDateTime eventDate,

        Long id,

        @NotNull
        UserShortDto initiator,

        @NotNull
        Location location,

        boolean paid,

        Integer participantLimit,

        LocalDateTime publishedOn,

        boolean requestModeration,

        EventState state,

        @NotBlank
        String title,

        Long views
) {
}