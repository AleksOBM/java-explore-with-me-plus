package ru.practicum.ewm.util.statistic;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.util.error.exception.HitRequestException;
import ru.practicum.ewm.util.error.exception.StatResponseException;
import ru.practicum.stat.client.StatClient;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.StatsRequest;
import ru.practicum.stat.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatRepository {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${app.name}")
    String appName;

    private final StatClient statClient;

    public void sendHitRequest(HttpServletRequest request) {
        try {
            statClient.hit(EndpointHitDto.builder()
                    .ip(request.getRemoteAddr())
                    .uri(request.getRequestURI())
                    .app(appName)
                    .timestamp(LocalDateTime.now())
                    .build()
            );
        } catch (Exception ex) {
            throw new HitRequestException(ex);
        }
    }

    public List<ViewStatsDto> getStat(List<String> statUris, LocalDateTime start, LocalDateTime end, boolean uniqe) {
        if (statUris == null || statUris.isEmpty()) {
            throw new StatResponseException();
        }

        StatsRequest request = StatsRequest.builder()
                .uris(statUris)
                .start(start.truncatedTo(ChronoUnit.MILLIS).format(formatter))
                .end(end.truncatedTo(ChronoUnit.MILLIS).format(formatter))
                .unique(uniqe)
                .build();

        List<ViewStatsDto> stats;
        try {
            stats = statClient.getStat(request);
        } catch (Exception ex) {
            throw new StatResponseException(ex);
        }

        if (stats == null) {
            throw new StatResponseException();
        }

        return stats;
    }

    public List<ViewStatsDto> getStat(List<String> statUris) {
        if (statUris == null || statUris.isEmpty()) {
            throw new StatResponseException();
        }

        LocalDateTime startUnixEpoch = LocalDateTime.parse("1970-01-01T00:00:00");
        StatsRequest request = StatsRequest.builder()
                .uris(statUris)
                .start(startUnixEpoch.truncatedTo(ChronoUnit.MILLIS).format(formatter))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS).format(formatter))
                .unique(true)
                .build();

        List<ViewStatsDto> stats;
        try {
            stats = statClient.getStat(request);
        } catch (Exception ex) {
            throw new StatResponseException(ex);
        }

        if (stats == null) {
            throw new StatResponseException();
        }

        return stats;
    }
}
