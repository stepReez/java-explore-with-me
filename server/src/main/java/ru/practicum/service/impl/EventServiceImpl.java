package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.request.*;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.EventService;
import ru.practicum.util.EventState;
import ru.practicum.util.EventsSort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                        String rangeStart, String rangeEnd, int from, int size) {
        return null;
    }

    @Override
    public EventFullDto patchEvent(long eventId, UpdateEventAdminRequest updateEvent) {
        return null;
    }

    @Override
    public List<EventShortDto> getEventsByUser(long userId, int from, int size) {
        return null;
    }

    @Override
    public EventFullDto createEvent(long userId, NewEventDto newEventDto) {
        return null;
    }

    @Override
    public EventFullDto getEventByUser(long userId, long eventId) {
        return null;
    }

    @Override
    public EventFullDto patchEventByUser(long userId, long eventId, UpdateEventUserRequest userRequest) {
        return null;
    }

    @Override
    public List<ParticipationRequestDto> getParticipation(long userId, long eventId) {
        return null;
    }

    @Override
    public EventRequestStatusUpdateResult patchParticipation(long userId, long eventId,
                                                             EventRequestStatusUpdateRequest statusUpdateRequest) {
        return null;
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, boolean paid, String rangeStart,
                                               String rangeEnd, boolean onlyAvailable, EventsSort sort,
                                               int from, int size) {
        return null;
    }

    @Override
    public EventFullDto getEventPublic(long id) {
        return null;
    }
}
