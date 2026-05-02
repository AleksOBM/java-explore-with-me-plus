package ru.practicum.ewm.util.statistic;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.util.error.exception.HitRequestException;
import ru.practicum.stat.client.StatClient;
import ru.practicum.stat.dto.EndpointHitDto;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class StatRepository {

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
}
