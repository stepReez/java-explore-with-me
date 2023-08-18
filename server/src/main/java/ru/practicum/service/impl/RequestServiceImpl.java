package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.RequestService;
import ru.practicum.util.EventState;
import ru.practicum.util.mapper.ParticipationRequestMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final ParticipationRepository participationRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getParticipationRequest(long userId) {
        List<ParticipationRequestDto> requests = participationRepository.findAllByUserId(userId).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        log.info("Participation requests for user {} found", userId);
        return requests;
    }

    @Override
    public ParticipationRequestDto createParticipationRequest(long userId, long eventId) {
        if (participationRepository.findAllByUserIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException("Request already exist");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("Requester can't be initiator");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event not published");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipants().size() == event.getParticipantLimit()) {
            throw new ConflictException("Participation limit reached");
        }
        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found")))
                .status(EventState.PENDING.toString())
                .build();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequest.setStatus(EventState.CONFIRMED.toString());
        }
        ParticipationRequest participationRequestDto = participationRepository.save(participationRequest);
        log.info("Participation requests with id = {} saved", participationRequestDto.getId());
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestDto);
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(long userId, long requestId) {
        ParticipationRequest participationRequest = participationRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Request not found"));
        if (participationRequest.getRequester().getId() == userId) {
            participationRequest.setStatus(EventState.CANCELED.toString());
        }
        ParticipationRequest participationRequestDto = participationRepository.save(participationRequest);
        log.info("Participation requests with id = {} canceled", requestId);
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestDto);
    }
}
