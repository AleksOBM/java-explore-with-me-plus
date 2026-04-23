package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsRequest {

    @NotNull
    private String start;

    @NotNull
    private String end;

    private List<String> uris;

    private Boolean unique = false;
}