package ru.practicum.ewm.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class CompilationMapper {

	public CompilationDto toCompilationDto(@NonNull Compilation compilation,
	                                       @NonNull Map<Long, Long> confirmedRequests,
	                                       @NonNull Map<Long, Long> views) {
		long id = compilation.getId();
		return CompilationDto.builder()
				.id(id)
				.pinned(compilation.isPinned())
				.title(compilation.getTitle())
				.events(compilation.getEvents().stream()
						.map(event ->
								EventMapper.toEventShortDto(
										event, confirmedRequests.get(id),
										views.get(id)
								))
						.toList())
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
