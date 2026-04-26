package ru.practicum.ewm.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.practicum.ewm.util.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "locations")
@SuperBuilder
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
