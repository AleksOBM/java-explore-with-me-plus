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

import java.util.*;

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

		return CompilationMapper.toCompilationDto(
				compilation,
				getConfirmedRequests(compilation),
				getViews(compilation)
		);
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

		Map<Long, Long> confirmedRequests = new HashMap<>();
		savedCompilation.getEvents().forEach(event -> confirmedRequests.put(event.getId(), 0L));

		Map<Long, Long> views = new HashMap<>();
		savedCompilation.getEvents().forEach(event -> views.put(event.getId(), 0L));

		return CompilationMapper.toCompilationDto(
				savedCompilation,
				confirmedRequests,
				views
		);
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

		return CompilationMapper.toCompilationDto(
				compilationInDb, getConfirmedRequests(compilationInDb), getViews(compilationInDb));
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
				.map(compilation -> CompilationMapper
						.toCompilationDto(compilation,
								getConfirmedRequests(compilation),
								getViews(compilation)
						)
				).toList();
	}

	@NonNull
	private Compilation getCompilationById(long compilationId) {
		return compilationRepository.findById(compilationId).orElseThrow(
				() -> new NotFoundException("Подборка с id=" + compilationId + " не найдена")
		);
	}

	/// Map<eventId, confirmedRequests>
	@NonNull
	private Map<Long, Long> getConfirmedRequests(Compilation compilation) {
		Set<Event> events = compilation.getEvents();
		if (events.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<Long, Long> confirmedRequests = new HashMap<>();
		for (Event event : events) {
			// todo
			confirmedRequests.put(event.getId(), 0L); // вместо 0 должно быть значение из базы
		}
		return confirmedRequests;
	}

	/// Map<eventId, views>
	@NonNull
	private Map<Long, Long> getViews(Compilation compilation) {
		Set<Event> events = compilation.getEvents();
		if (events.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<Long, Long> views = new HashMap<>();
		for (Event event : events) {
			// todo
			views.put(event.getId(), 0L); // вместо 0 должно быть значение из базы
		}
		return views;
	}
}
