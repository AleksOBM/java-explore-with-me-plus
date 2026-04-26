package ru.practicum.ewm.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;
import ru.practicum.ewm.dto.EventLocationDto;
import ru.practicum.ewm.model.EventLocation;

@UtilityClass
public class LocationMapper {

	public EventLocation fromDto(@NonNull EventLocationDto locationDto) {
		return EventLocation.builder()
				.lat(locationDto.lat())
				.lon(locationDto.lon())
				.build();
	}

	public EventLocationDto toDto(@NonNull EventLocation location) {
		return EventLocationDto.builder()
				.lat(location.getLat())
				.lon(location.getLon())
				.build();
	}
}
