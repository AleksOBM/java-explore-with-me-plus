package ru.practicum.ewm.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.service.CompilationService;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delById(@PathVariable Long compilationId) {
        compilationService.delById(compilationId);
    }

    @PatchMapping("/{compilationId}")
    public CompilationDto updateCompilation(@RequestBody @Valid CompilationUpdateDto compilationUpdateDto,
                                            @PathVariable Long compilationId) {
        return compilationService.updateCompilation(compilationId ,compilationUpdateDto);
    }
}
