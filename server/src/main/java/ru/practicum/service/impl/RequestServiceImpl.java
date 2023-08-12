package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.repository.ParticipationRepository;
import ru.practicum.service.RequestService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final ParticipationRepository participationRepository;

    @Override
    public List<ParticipationRequestDto> getParticipationRequest(long userId) {
        return null;
    }

    @Override
    public ParticipationRequestDto createParticipationRequest(long userId, long eventId) {
        return null;
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(long userId, long requestId) {
        return null;
    }
}
