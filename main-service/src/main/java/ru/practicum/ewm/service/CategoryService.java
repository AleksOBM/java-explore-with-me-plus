package ru.practicum.ewm.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;

public interface CategoryService {

	@Transactional
	CategoryDto adminAddNewCategory(NewCategoryDto newCategoryDto);
}
