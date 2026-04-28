package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.CompilationSearchFilter;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.dto.CompilationUpdateDto;
import java.util.List;

public interface CompilationService {

    CompilationDto getById(Long compilationId, HttpServletRequest request);

    void delById(Long compilationId);

    CompilationDto addCompilation(NewCompilationDto compilation);

    CompilationDto updateCompilation(Long compilationId, CompilationUpdateDto compilation);

    List<CompilationDto> getByFilter(CompilationSearchFilter filter, HttpServletRequest request);

}
