package ru.practicum.stat.server.mapper;

import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.server.model.EndpointHit;

public class EndpointHitMapper {

    public static EndpointHit toEntity(EndpointHitDto dto) {
        return EndpointHit.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();
    }

    public static EndpointHitDto toDto(EndpointHit entity) {
        return EndpointHitDto.builder()
                .app(entity.getApp())
                .uri(entity.getUri())
                .ip(entity.getIp())
                .timestamp(entity.getTimestamp())
                .actionType("VIEW")
                .build();
    }
}
