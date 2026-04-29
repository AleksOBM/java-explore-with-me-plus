package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ru.practicum.ewm.model.EventState;

import java.util.List;

@Builder
public record EventRequestStatusUpdateRequest(
        List<Long> requestIds,
        @NotNull
        EventState status
) {
}
