package ru.practicum.ewm.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.practicum.ewm.util.entity.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "events")
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event extends BaseEntity {

	@Column(nullable = false,  length = 2000)
	String annotation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	EventCategory category;

	@Column(nullable = false)
	int confirmedRequests;

	@Column(nullable = false)
	LocalDateTime createdOn;

	@Column(nullable = false, length = 7000)
	String description;

	@Column(nullable = false)
	LocalDateTime eventDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "initiator_id")
	User initiator;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "location_id", referencedColumnName = "id")
	EventLocation location;

	@Column(nullable = false)
	boolean paid;

	@Column(nullable = false)
	int participantLimit;

	@Column(nullable = false)
	LocalDateTime publishedOn;

	@Column(nullable = false)
	boolean requestModeration;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	EventState state;

	@Column(nullable = false, length = 120)
	String title;

	@Column(nullable = false)
	long views;
}
