package ru.practicum.ewm.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import ru.practicum.ewm.util.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "events")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event extends BaseEntity {

	@Column(nullable = false,  length = 2000)
	String annotation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	Category category;

	@Column(nullable = false)
	Long confirmedRequests;

	@Column(nullable = false)
	LocalDateTime createdOn;

	@Column(nullable = false, length = 7000)
	String description;

	@Column(nullable = false)
	LocalDateTime eventDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "initiator_id")
	User initiator;

	@Type(JsonBinaryType.class)
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	Location location;

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

	@ManyToMany(mappedBy = "events")
	List<Compilation> compilations;
}
