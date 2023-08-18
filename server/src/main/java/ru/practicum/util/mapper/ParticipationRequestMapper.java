package ru.practicum.util.mapper;

import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.util.Constants;

import java.time.format.DateTimeFormatter;

public class ParticipationRequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .requester(participationRequest.getRequester().getId())
                .event(participationRequest.getEvent().getId())
                .status(participationRequest.getStatus())
                .created(participationRequest.getCreated()
                        .format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)))
                .build();
    }
}
