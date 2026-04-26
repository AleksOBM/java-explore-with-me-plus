package ru.practicum.ewm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.EventCategory;

public interface CategoryRepository extends JpaRepository<EventCategory, Long> {
}
