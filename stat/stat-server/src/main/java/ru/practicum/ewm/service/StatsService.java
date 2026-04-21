package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.StatDto;
import ru.practicum.ewm.dao.EventRepository;
import ru.practicum.ewm.model.Event;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {

    private final EventRepository eventRepository;

    @Transactional
    public void saveHit(Event event) {
        log.debug("Сохранение записи о просмотре для URI: {}", event.getUri());
        eventRepository.save(event);
    }

    public List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (unique) {
            log.debug("Получение статистики (уникальные IP) для URI: {}", uris);
            return eventRepository.getStatsUnique(start, end, uris);
        }

        log.debug("Получение общей статистики для URI: {}", uris);
        return eventRepository.getStats(start, end, uris);
    }
}
