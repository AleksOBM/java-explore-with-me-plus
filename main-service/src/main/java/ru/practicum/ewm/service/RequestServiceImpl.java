package ru.practicum.ewm.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import ru.practicum.ewm.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.ParticipationRequestDto;

import java.util.List;

public class RequestService {
    public List<ParticipationRequestDto> findByEventId(Long userId, Long eventId) {
        return null;
    }

    public EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId,
                                                                           EventRequestStatusUpdateRequest request) {
        return null;
    }

    public List<ParticipationRequestDto> findByRequesterId(Long userId) {
        return null;
    }

    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        return null;
    }

    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        return null;
    }
}
