package ru.practicum.service;

import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getParticipationRequest(long userId);

    ParticipationRequestDto createParticipationRequest(long userId, long eventId);

    ParticipationRequestDto cancelParticipationRequest(long userId, long requestId);
}
