package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dao.CategoryRepository;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.util.error.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto adminAddNewCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toEntity(newCategoryDto);
        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public Category findEntityById(Long category) {
        return categoryRepository.findById(category)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + category + " не найдена"));
    }

    @Override
    public List<CategoryDto> findAll(Integer from, Integer size) {
        int page = from / size;

        return categoryRepository.findAll(PageRequest.of(page, size)).stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(Long catId) {
        Category category = findEntityById(catId);

        return CategoryMapper.toDto(category);
    }
}
