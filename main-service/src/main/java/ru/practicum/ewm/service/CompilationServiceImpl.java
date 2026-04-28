package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dao.CompilationRepository;
import ru.practicum.ewm.dao.EventRepository;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.CompilationSearchFilter;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.dto.UpdateCompilationDto;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.util.UtilService;
import ru.practicum.ewm.util.error.exception.NotFoundException;
import ru.practicum.ewm.util.statistic.StatService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final UtilService utilService;
    private final StatService statService;

    @Override
    public CompilationDto getById(Long compilationId, HttpServletRequest request) {
        statService.sendHitRequest(request);

        Compilation compilation = utilService.getCompilationById(compilationId);

        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public void delById(Long compilationId) {
        utilService.getCompilationById(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
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
    public CompilationDto updateCompilation(Long compilationId, UpdateCompilationDto compilation) {
        return null;
    }

    @Override
    public List<CompilationDto> getByFilter(CompilationSearchFilter filter, HttpServletRequest request) {
        return List.of();
    }
}
