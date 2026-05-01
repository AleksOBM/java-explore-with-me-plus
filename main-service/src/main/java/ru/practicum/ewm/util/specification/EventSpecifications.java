package ru.practicum.ewm.util.specification;

import jakarta.persistence.criteria.Join;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.enums.EventState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@UtilityClass
public class EventSpecifications {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /// Поиск по тексту
    public Specification<Event> textContains(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isBlank()) {
                return null;
            }

            String pattern = "%" + text.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("annotation")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }

    /// Категории
    public Specification<Event> hasCategories(List<Integer> categories) {
        return (root, query, cb) -> {
            if (categories == null || categories.isEmpty()) {
                return null;
            }

            Join<Event, Category> categoryJoin = root.join("category");

            return categoryJoin.get("id").in(categories);
        };
    }

    /// Платное / бесплатное
    public Specification<Event> isPaid(Boolean paid) {
        return (root, query, cb) -> {
            if (paid == null) {
                return null;
            }

            return cb.equal(root.get("paid"), paid);
        };
    }

    /// Дата начала
    public Specification<Event> dateAfter(String rangeStart) {
        return (root, query, cb) -> {
            if (rangeStart == null) {
                return null;
            }

            LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);

            return cb.greaterThanOrEqualTo(root.get("eventDate"), start);
        };
    }

    /// Дата окончания
    public Specification<Event> dateBefore(String rangeEnd) {
        return (root, query, cb) -> {
            if (rangeEnd == null) {
                return null;
            }

            LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);

            return cb.lessThanOrEqualTo(root.get("eventDate"), end);
        };
    }

    /// Только доступные события
    public Specification<Event> onlyAvailable(Boolean onlyAvailable) {
        return (root, query, cb) -> {
            if (!Boolean.TRUE.equals(onlyAvailable)) {
                return null;
            }

            return cb.or(
                    cb.equal(root.get("participantLimit"), 0),
                    cb.lessThan(
                            root.get("confirmedRequests"),
                            root.get("participantLimit")
                    )
            );
        };
    }

    /// Статус = PUBLISHED
    public Specification<Event> isPublished() {
        return (root, query, cb) ->
                cb.equal(root.get("state"), EventState.PUBLISHED);
    }

    /// Дата в будущем
    public Specification<Event> eventDateAfterNow(LocalDateTime now) {
        return (root, query, cb) ->
                cb.greaterThan(root.get("eventDate"), now);
    }
}