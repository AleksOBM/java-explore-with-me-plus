package ru.practicum.ewm.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.practicum.ewm.util.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "locations")
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventLocation extends BaseEntity {

	@Builder.Default
	@Column(nullable = false)
	String lat = "00.00";

	@Builder.Default
	@Column(nullable = false)
	String lon = "00.00";

	@OneToOne(mappedBy = "location")
	Event event;
}
