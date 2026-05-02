package ru.practicum.ewm.service.compilation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dao.CompilationRepository;
import ru.practicum.ewm.dao.EventRepository;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.CompilationSearchFilter;
import ru.practicum.ewm.dto.compilation.CompilationUpdateDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.util.error.exception.NotFoundException;
import ru.practicum.ewm.util.statistic.StatRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationServiceImpl implements CompilationService {

	CompilationRepository compilationRepository;
	EventRepository eventRepository;
	StatRepository statRepository;

	@Override
	public CompilationDto getById(Long compilationId, HttpServletRequest request) {
		statRepository.sendHitRequest(request);

		Compilation compilation = getCompilationById(compilationId);

		return CompilationMapper.toCompilationDto(compilation);
	}

	@Override
	@Transactional
	public void delById(Long compilationId) {
		getCompilationById(compilationId);
		compilationRepository.deleteById(compilationId);
	}

	@Override
	@Transactional
	public CompilationDto addCompilation(@NonNull NewCompilationDto compilationDto) {
		Set<Event> events = new HashSet<>();

		if (compilationDto.getEvents() != null && !compilationDto.getEvents().isEmpty()) {
			events = new HashSet<>(eventRepository.findAllById(compilationDto.getEvents()));

			if (events.size() < compilationDto.getEvents().size()) {
				throw new NotFoundException("Одно или несколько событий не найдены");
			}
		}

		Compilation compilation = CompilationMapper.toEntity(compilationDto, events);
		Compilation savedCompilation = compilationRepository.save(compilation);

		return CompilationMapper.toCompilationDto(savedCompilation);
	}

	@Override
	@Transactional
	public CompilationDto updateCompilation(Long compilationId, @NonNull CompilationUpdateDto compilationUpdateDto) {
		Compilation compilationInDb = getCompilationById(compilationId);

		if (compilationUpdateDto.getEvents() != null) {
			List<Event> eventsUpdate = new ArrayList<>();

			if (!compilationUpdateDto.getEvents().isEmpty()) {
				eventsUpdate = eventRepository.findAllById(compilationUpdateDto.getEvents());
				if (eventsUpdate.size() < compilationUpdateDto.getEvents().size()) {
					throw new NotFoundException("Одно или несколько событий не найдены");
				}
			}
			compilationInDb.setEvents(new HashSet<>(eventsUpdate));

		}

		if (compilationUpdateDto.getTitle() != null && !compilationUpdateDto.getTitle().isBlank()) {
			compilationInDb.setTitle(compilationUpdateDto.getTitle());
		}

		if (compilationUpdateDto.getPinned() != null) {
			compilationInDb.setPinned(compilationUpdateDto.getPinned());
		}

		return CompilationMapper.toCompilationDto(compilationInDb);
	}

	@Override
	public List<CompilationDto> getByFilter(@NonNull CompilationSearchFilter filter, HttpServletRequest request) {
		Pageable pageable = PageRequest.of(filter.getFrom() / filter.getSize(), filter.getSize());
		Page<Compilation> compilations;

		if (filter.getPinned() != null) {
			compilations = compilationRepository.findAllByPinned(filter.getPinned(), pageable);
		} else {
			compilations = compilationRepository.findAll(pageable);
		}

		return compilations.getContent().stream()
				.map(CompilationMapper::toCompilationDto)
				.collect(Collectors.toList());
	}

	@NonNull
	private Compilation getCompilationById(long compilationId) {
		return compilationRepository.findById(compilationId).orElseThrow(
				() -> new NotFoundException("Подборка с id=" + compilationId + " не найдена")
		);
	}
}
