package ru.practicum.ewm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.practicum.ewm.util.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "categories")
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventCategory extends BaseEntity {

	@Column(nullable = false)
	String name;
}
