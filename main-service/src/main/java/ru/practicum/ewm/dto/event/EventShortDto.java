package ru.practicum.ewm.dto.event;

import lombok.Builder;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Builder
public record EventShortDto(
        String annotation,
        CategoryDto category,
        long confirmedRequests,
        LocalDateTime eventDate,
        long id,
        UserShortDto initiator,
        boolean paid,
        String title,
        long views
) {
}
