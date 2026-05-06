package ru.practicum.ewm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    Optional<Category> findByName(String name);
}
