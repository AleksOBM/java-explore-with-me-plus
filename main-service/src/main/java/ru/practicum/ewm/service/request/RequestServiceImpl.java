package ru.practicum.ewm.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dao.RequestRepository;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.mapper.RequestMapper;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.model.enums.ParticipationStatus;
import ru.practicum.ewm.util.UtilService;
import ru.practicum.ewm.util.error.exception.ConflictException;
import ru.practicum.ewm.util.error.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

	private final RequestRepository repository;
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
		Event event = utilService.getEventById(eventId);
		if (!event.getInitiator().getId().equals(userId)) {
			throw new NotFoundException("Событие не найдено");
		}

		int limit = event.getParticipantLimit();
		List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
		List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

		int countConfirmed = repository.countByEventIdAndStatus(eventId, ParticipationStatus.CONFIRMED);
		List<ParticipationRequest> requests = repository.findAllByIdIn(request.requestIds());

		// владелец хочет подтвердить, а лимит исчерпан
		if (request.status().name().equals(ParticipationStatus.CONFIRMED.name()) && countConfirmed >= limit) {
			throw new ConflictException("Достигнут лимит подтвержденных заявок");
		}

		for (ParticipationRequest pr : requests) {
			if (!ParticipationStatus.PENDING.equals(pr.getStatus())) {
				throw new ConflictException("Статус можно изменить только у заявок в состоянии рассмотрения");
			}

			if (request.status().name().equals(ParticipationStatus.CONFIRMED.name()) && countConfirmed < limit) {
				pr.setStatus(ParticipationStatus.CONFIRMED);
				countConfirmed++;
				confirmedRequests.add(RequestMapper.toParticipationRequestDto(pr));
			} else {
				pr.setStatus(ParticipationStatus.REJECTED);
				rejectedRequests.add(RequestMapper.toParticipationRequestDto(pr));
			}
		}

		repository.saveAll(requests);

		// если в процессе лимит превышен - отклоняем все оставшиеся заявки
		if (request.status().name().equals(ParticipationStatus.CONFIRMED.name()) && countConfirmed >= limit) {
			if (repository.rejectPendingRequests(eventId, ParticipationStatus.PENDING) < 0) {
				throw new RuntimeException("Не удалось отклонить заявку");
			}
		}

		return EventRequestStatusUpdateResult.builder()
				.confirmedRequests(confirmedRequests)
				.rejectedRequests(rejectedRequests)
				.build();
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

		int limit = event.getParticipantLimit();

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
