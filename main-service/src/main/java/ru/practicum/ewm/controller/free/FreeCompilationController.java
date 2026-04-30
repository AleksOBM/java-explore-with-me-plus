package ru.practicum.ewm.controller.free;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.CompilationSearchFilter;
import ru.practicum.ewm.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class FreeCompilationController {
	private final CompilationService compilationService;

	@GetMapping
	public List<CompilationDto> getCompilations(
			@RequestParam(required = false) Boolean pinned,
			@RequestParam(defaultValue = "0") Integer from,
			@RequestParam(defaultValue = "10") Integer size,
			HttpServletRequest request) {

		CompilationSearchFilter filter = CompilationSearchFilter.builder()
				.pinned(pinned)
				.from(from)
				.size(size)
				.build();

		return compilationService.getByFilter(filter, request);
	}

	@GetMapping("/{compilationId}")
	public CompilationDto getCompilationById(@PathVariable Long compilationId,
	                                         HttpServletRequest request) {
		return compilationService.getById(compilationId, request);
	}
}
