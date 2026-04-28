package ru.practicum.ewm.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {

    public CompilationDto toCompilationDto(@NonNull Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList()))
                .build();
    }


    public Compilation toEntity(@NonNull NewCompilationDto dto, Set<Event> events) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.isPinned())
                .events(events != null ? events : new HashSet<>())
                .build();
    }
}
