package ru.practicum.ewm.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.model.Category;

public interface CategoryService {

    @Transactional
    CategoryDto adminAddNewCategory(NewCategoryDto newCategoryDto);

    Category findEntityById(@NotNull Long category);

    @Transactional
    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);

    @Transactional
    void deleteCategory(Long catId);
}
