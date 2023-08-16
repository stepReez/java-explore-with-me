package ru.practicum.service;

import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.UpdateEventAdminRequest;
import ru.practicum.dto.request.UpdateEventUserRequest;
import ru.practicum.util.EventState;
import ru.practicum.util.EventsSort;

import java.util.List;

public interface EventService {
    List<EventFullDto> getEvents(List<Long> users,
                                 List<EventState> states,
                                 List<Long> categories,
                                 String rangeStart,
                                 String rangeEnd,
                                 int from,
                                 int size);

    EventFullDto patchEvent(long eventId, UpdateEventAdminRequest updateEvent);

    List<EventShortDto> getEventsByUser(long userId, int from, int size);

    EventFullDto createEvent(long userId, NewEventDto newEventDto);

    EventFullDto getEventByUser(long userId, long eventId);

    EventFullDto patchEventByUser(long userId, long eventId, UpdateEventUserRequest userRequest);

    List<ParticipationRequestDto> getParticipation(long userId,  long eventId);

    EventRequestStatusUpdateResult patchParticipation(long userId, long eventId,
                                                      EventRequestStatusUpdateRequest statusUpdateRequest);

    List<EventShortDto> getEventsPublic(String text,
                                        List<Long> categories,
                                        Boolean paid,
                                        String rangeStart,
                                        String rangeEnd,
                                        boolean onlyAvailable,
                                        EventsSort sort,
                                        int from,
                                        int size);

    EventFullDto getEventPublic(long id);
}
