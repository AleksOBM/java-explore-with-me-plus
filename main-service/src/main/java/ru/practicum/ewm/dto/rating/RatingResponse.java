package ru.practicum.ewm.dto.rating;

import lombok.*;
import ru.practicum.ewm.model.enums.Reaction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {
    private Long id;
    private Long userId;
    private Long eventId;
    private Reaction reaction;
}
