package ru.practicum.ewm.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.practicum.ewm.util.entity.BaseEntity;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "compilations")
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation extends BaseEntity {

	@Column(nullable = false)
	String title;

	@ManyToMany
	@JoinTable(name = "compilation_events")
	List<Event> events;

	boolean pinned;
}
