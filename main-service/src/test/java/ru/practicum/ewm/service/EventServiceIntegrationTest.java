package ru.practicum.ewm.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.practicum.ewm.MainServiceApp;
import ru.practicum.ewm.TestUtils;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.FreeGetDto;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.util.statistic.StatRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
@EnableAutoConfiguration(exclude = {
		WebMvcAutoConfiguration.class
})
@FieldDefaults(level = AccessLevel.PRIVATE)
@ContextConfiguration(classes = MainServiceApp.class)
class EventServiceIntegrationTest {

	@MockitoBean
	StatRepository statRepository;

	@Autowired
	EventService eventService;

	@Autowired
	TestUtils testUtils;

	@PersistenceContext
	EntityManager em;

	private Event mergeEvent(Event event) {
		event.setInitiator(em.merge(event.getInitiator()));
		event.setCategory(em.merge(event.getCategory()));
		return em.merge(event);
	}

	@Nested
	@DisplayName("Получение публичного события по поиску")
	class GetFreeEvents {

		@Test
		void whenBaseFlow_thenReturnOnlyPublishedAndFutureEvents() {
			// region setup
			Event event = mergeEvent(
					testUtils.generateEvent(testUtils.futureDate, EventState.PUBLISHED)
			);
			mergeEvent(testUtils.getNewCopyOfEvent(event, EventState.PENDING));
			mergeEvent(testUtils.getNewCopyOfEvent(event, EventState.CANCELED));
			mergeEvent(testUtils.getNewCopyOfEvent(event, EventState.CANCELED))
					.toBuilder().eventDate(testUtils.pastDate).build();

			FreeGetDto dto = FreeGetDto.builder().from(0).size(10).build();
			// endregion setup

			doNothing().when(statRepository).sendHitRequest(any(HttpServletRequest.class));

			List<EventShortDto> result =
					eventService.getFreeEvents(dto, mock(HttpServletRequest.class));

			assertThat(result).hasSize(1);
			assertThat(result).contains(
					EventMapper.toEventShortDto(
							event.toBuilder().id(1L).build(),
							0L,
							0L
					)
			);
			verify(statRepository).sendHitRequest(any(HttpServletRequest.class));
		}
	}
}