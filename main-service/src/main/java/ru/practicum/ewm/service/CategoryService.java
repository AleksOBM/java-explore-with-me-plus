package ru.practicum.ewm.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.EventCategoryDto;

public interface CategoryService {

	@Transactional
	EventCategoryDto adminAddNewCategory(EventCategoryDto eventCategoryDto);
}
