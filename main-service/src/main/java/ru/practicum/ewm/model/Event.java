package ru.practicum.ewm.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event extends BaseEntity {

	@Column(nullable = false)
	String annotation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	EventCategory category;

	@Column(nullable = false)
	int confirmedRequests;

	@Column(nullable = false)
	LocalDateTime createdOn;

	@Column(nullable = false)
	String description;

	@Column(nullable = false)
	LocalDateTime eventDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "initiator_id")
	User initiator;

	@Column(nullable = false)
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

	@Column(nullable = false)
	String title;

	@Column(nullable = false)
	long views;
}
