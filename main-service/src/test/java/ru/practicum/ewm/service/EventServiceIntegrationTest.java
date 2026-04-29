package ru.practicum.ewm.service;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.practicum.ewm.MainServiceApp;
import ru.practicum.ewm.dao.CategoryRepository;
import ru.practicum.ewm.dao.UserRepository;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.FreeGetDto;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.util.statistic.StatService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@ContextConfiguration(classes = MainServiceApp.class)
class EventServiceIntegrationTest {

    @MockitoBean
    StatService statService;

    @Autowired
    private EventService eventService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EntityManager em;

    @Test
    void shouldReturnOnlyPublishedAndFutureEvents() {
        // region setup
        User initiator = userRepository.save(User.builder().name("User").email("user@email.ry").build());
        Category category = categoryRepository.save(Category.builder().name("Category").build());
        Event event = Event.builder()
                .annotation("annotation".repeat(10))
                .category(category)
                .confirmedRequests(10L)
                .createdOn(LocalDateTime.parse("2026-11-10T19:00:00"))
                .description("description".repeat(10))
                .eventDate(LocalDateTime.parse("2027-01-01T00:00:01"))
                .initiator(initiator)
                .location(Location.builder().lat(25.55F).lon(35.77F).build())
                .paid(true)
                .participantLimit(30)
                .publishedOn(LocalDateTime.parse("2026-12-10T21:00:00"))
                .requestModeration(true)
                .state(EventState.PUBLISHED)
                .title("title")
                .views(1000L)
                .build();

        Event publishedFuture = event.toBuilder().state(EventState.PUBLISHED).build();
        Event pendingFuture = event.toBuilder().state(EventState.PENDING).build();
        Event canceledFuture = event.toBuilder().state(EventState.CANCELED).build();
        Event publishedPast = event.toBuilder()
                .eventDate(LocalDateTime.parse("2025-01-01T00:00:01"))
                .state(EventState.PUBLISHED)
                .build();

        FreeGetDto dto = FreeGetDto.builder()
                .from(0)
                .size(10)
                .build();

        em.merge(publishedFuture);
        em.merge(pendingFuture);
        em.merge(canceledFuture);
        em.merge(publishedPast);
        // endregion setup

        doNothing().when(statService).sendHitRequest(any(HttpServletRequest.class));

        List<EventShortDto> result =
                eventService.getFreeEvents(dto, mock(HttpServletRequest.class));

        assertThat(result).hasSize(1);
        verify(statService).sendHitRequest(any(HttpServletRequest.class));
    }
}