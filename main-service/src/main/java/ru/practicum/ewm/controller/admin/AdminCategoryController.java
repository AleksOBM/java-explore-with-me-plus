package ru.practicum.ewm.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.service.CategoryService;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @SuppressWarnings("checkstyle:AnnotationLocation")
    @PostMapping
	@ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addNewCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.adminAddNewCategory(newCategoryDto);
    }

	@PatchMapping("/{catId}")
	public CategoryDto updateCategory(@PathVariable Long catId,
									  @Valid @RequestBody CategoryDto categoryDto) {
		return categoryService.updateCategory(catId, categoryDto);
	}

	@DeleteMapping("/{catId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCategory(@PathVariable Long catId) {
		categoryService.deleteCategory(catId);
	}
}
