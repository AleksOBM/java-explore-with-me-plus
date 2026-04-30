package ru.practicum.ewm.service;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.model.Category;

import java.util.List;

public interface CategoryService {

    @Transactional
    CategoryDto adminAddNewCategory(NewCategoryDto newCategoryDto);

    Category findEntityById(Long category);

    List<CategoryDto> findAll(Integer from, Integer size);

    CategoryDto findById(Long catId);
}
