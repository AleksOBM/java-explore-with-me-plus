package ru.practicum.ewm.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    private Set<Long> events;

    private boolean pinned = false;

    @NotBlank
    @Size(min = 1, max = 255)
    private String title;
}