package ru.practicum.ewm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatDto {
    private String app;
    private String uri;
    private Long hits;
}
