package ru.practicum.ewm.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;
import ru.practicum.ewm.dto.EventCategoryDto;
import ru.practicum.ewm.model.EventCategory;

@UtilityClass
public class CategoryMapper {
	public EventCategoryDto toDto(@NonNull EventCategory eventCategory) {
		return EventCategoryDto.builder()
				.id(eventCategory.getId())
				.name(eventCategory.getName())
				.build();
	}

	public EventCategory toEntity(@NonNull EventCategoryDto eventCategoryDto) {
		return EventCategory.builder()
				.name(eventCategoryDto.name())
				.build();
	}
}
