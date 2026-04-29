package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dao.RequestRepository;
import ru.practicum.ewm.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.mapper.RequestMapper;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.util.UtilService;
import ru.practicum.ewm.util.error.exception.ConflictException;
import ru.practicum.ewm.util.error.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository repository;
    private final EventService eventService;
    private final UtilService utilService;

    public List<ParticipationRequestDto> findByEventId(Long userId, Long eventId) {
        Event event = utilService.getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Событие не найдено");
        }

        return repository.findByEventId(eventId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .toList();
    }

    public EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId,
                                                                           EventRequestStatusUpdateRequest request) {
        return null;
    }

    public List<ParticipationRequestDto> findByRequesterId(Long userId) {
        return repository.findByRequesterId(userId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .toList();
    }

    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        User requester = utilService.getUserById(userId);
        Event event = utilService.getEventById(eventId);

        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }

        if (repository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Запрос уже существует");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Инициатор события не может добавить запрос на участие в своём событии");
        }

        Integer limit = event.getParticipantLimit();

        if (limit != 0 && repository.countByEventIdAndStatus(eventId, ParticipationStatus.CONFIRMED) >= limit) {
            throw new ConflictException("Достигнут лимит запросов на участие");
        }

        ParticipationRequest request = ParticipationRequest.builder()
                .requester(requester)
                .event(event)
                .status(ParticipationStatus.PENDING)
                .build();
        /* заглушка на проход модерации
        if (!event.getRequestModeration() || limit == 0) {
            request.setStatus(ParticipationStatus.CONFIRMED);
        }
        */

        return RequestMapper.toParticipationRequestDto(repository.save(request));
    }

    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        ParticipationRequest request = repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));

        if (!request.getRequester().getId().equals(userId)) {
            throw new ConflictException("Нельзя отменить чужую заявку");
        }
        request.setStatus(ParticipationStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(repository.save(request));
    }
}
