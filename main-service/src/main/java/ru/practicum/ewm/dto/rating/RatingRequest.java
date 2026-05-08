package ru.practicum.ewm.dto.rating;

import lombok.*;
import ru.practicum.ewm.model.enums.Reaction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {
    private Reaction reaction;
}
