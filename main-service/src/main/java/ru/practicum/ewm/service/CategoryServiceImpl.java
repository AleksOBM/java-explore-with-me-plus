package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dao.CategoryRepository;
import ru.practicum.ewm.dto.EventCategoryDto;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.EventCategory;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	@Override
	public EventCategoryDto adminAddNewCategory(EventCategoryDto eventCategoryDto) {
		EventCategory category = CategoryMapper.toEntity(eventCategoryDto);
		return CategoryMapper.toDto(categoryRepository.save(category));
	}
}
