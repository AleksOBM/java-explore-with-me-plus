package ru.practicum.ewm;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.StatsRequest;
import ru.practicum.ewm.dto.ViewStatsDto;

import java.net.URI;
import java.util.List;

@Slf4j
@Component
public class StatClient {

    @Value("${stat-service.url}")
    private String serverUrl;

    private final RestTemplate rest;

    public StatClient(RestTemplateBuilder builder) {
        this.rest = builder.build();
    }

    public void hit(EndpointHitDto endpointHitDto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EndpointHitDto> requestEntity = new HttpEntity<>(endpointHitDto, headers);

            rest.exchange(
                    serverUrl + "/hit",
                    HttpMethod.POST,
                    requestEntity,
                    Void.class
            );
        } catch (Exception e) {
            log.error("Ошибка записи: {}", endpointHitDto, e);
        }

    }

    public List<ViewStatsDto> getStat(StatsRequest statsRequest) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serverUrl + "/stats")
                    .queryParam("start", statsRequest.getStart())
                    .queryParam("end", statsRequest.getEnd())
                    .queryParam("unique", statsRequest.getUnique());

            List<String> uris = statsRequest.getUris();

            if (uris != null && !uris.isEmpty()) {
                builder.queryParam("uris", uris);
            }

            URI uri = builder.encode().build().toUri();

            return rest.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ViewStatsDto>>() {
                    }
            ).getBody();

        } catch (Exception e) {
            log.error("Ошибка записи: {}", statsRequest, e);
            return null;
        }
    }
}
