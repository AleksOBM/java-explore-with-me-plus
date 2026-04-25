package ru.practicum.ewm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
	String annotation;
	EventCategory category;
	int confirmedRequests;
	LocalDateTime createdOn;
	String description;
	LocalDateTime eventDate;
	User initiator;
	EventLocation location;
	boolean paid;
	int participantLimit;
	LocalDateTime publishedOn;
	boolean requestModeration;
	EventState state;
	String title;
	long views;
}
