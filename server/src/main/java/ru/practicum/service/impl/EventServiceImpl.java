package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.request.*;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.EventService;
import ru.practicum.util.Constants;
import ru.practicum.util.EventState;
import ru.practicum.util.EventsSort;
import ru.practicum.util.mapper.EventMapper;
import ru.practicum.util.mapper.ParticipationRequestMapper;

import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final ParticipationRepository participationRepository;

    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                        String rangeStart, String rangeEnd, int from, int size) {
        LocalDateTime startDate = LocalDateTime.parse(
                URLDecoder.decode(rangeStart, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)
        );
        LocalDateTime endDate = LocalDateTime.parse(
                URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)
        );
        List<EventFullDto> events = eventRepository.findAdmin(users, states, categories,
                startDate, endDate, PageRequest.of(from / size, size)).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
        log.info("All necessary events found");
        return events;
    }

    @Override
    public EventFullDto patchEvent(long eventId, Map<Object, Object> updateEvent) {
        Event event = eventRepository.findById(eventId).orElseThrow();

        updateEvent.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Event.class, (String) key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, event, value);
        });

        Event newEvent = eventRepository.save(event);
        log.info("Event with id = {} patched", eventId);
        return EventMapper.toEventFullDto(newEvent);
    }

    @Override
    public List<EventShortDto> getEventsByUser(long userId, int from, int size) {
        List<EventShortDto> events = eventRepository.findByUser(userId, PageRequest.of(from / size, size)).stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
        log.info("Events for user {} found", userId);
        return events;
    }

    @Override
    public EventFullDto createEvent(long userId, NewEventDto newEventDto) {
        Event event = EventMapper.toEvent(newEventDto,
                categoryRepository.findById(newEventDto.getCategory()).orElseThrow());
        event.setCreated(LocalDateTime.now());
        event.setInitiator(userRepository.findById(userId).orElseThrow());
        Event eventFullDto = eventRepository.save(event);
        log.info("Event with id = {} saved", eventFullDto.getId());
        return EventMapper.toEventFullDto(eventFullDto);
    }

    @Override
    public EventFullDto getEventByUser(long userId, long eventId) {
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.findById(eventId).orElseThrow());
        log.info("Event with id = {} found", eventId);
        return eventFullDto;
    }

    @Override
    public EventFullDto patchEventByUser(long userId, long eventId, Map<Object, Object> userRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow();

        userRequest.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Event.class, (String) key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, event, value);
        });

        Event newEvent = eventRepository.save(event);
        log.info("Event with id = {} saved", eventId);
        return EventMapper.toEventFullDto(newEvent);
    }

    @Override
    public List<ParticipationRequestDto> getParticipation(long userId, long eventId) {
        List<ParticipationRequestDto> requests = participationRepository.findAllByUserIdAndEventId(userId, eventId)
                .stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        log.info("Participation for user {} found", userId);
        return null;
    }

    @Override
    public EventRequestStatusUpdateResult patchParticipation(long userId, long eventId,
                                                             EventRequestStatusUpdateRequest statusUpdateRequest) {
        statusUpdateRequest.getRequestIds().forEach((x) -> {
            List<ParticipationRequest> participationRequest = participationRepository.findAllByUserIdAndEventId(x, eventId);
            participationRequest.forEach((request) -> request.setStatus(statusUpdateRequest.getStatus().toString()));
            participationRequest.forEach((participationRepository::save));
        });
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> participationRequestsConfirmed = participationRepository.findAllByEventId(eventId)
                .stream()
                .filter(request -> request.getStatus().equals("CONFIRMED"))
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());

        List<ParticipationRequestDto> participationRequestsRejected = participationRepository.findAllByEventId(eventId)
                .stream()
                .filter(request -> request.getStatus().equals("REJECTED"))
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());

        eventRequestStatusUpdateResult.setConfirmedRequests(participationRequestsConfirmed);
        eventRequestStatusUpdateResult.setRejectedRequests(participationRequestsRejected);

        log.info("Participations patched");
        return eventRequestStatusUpdateResult;
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, boolean paid, String rangeStart,
                                               String rangeEnd, boolean onlyAvailable, EventsSort sort,
                                               int from, int size) {
        LocalDateTime startDate = LocalDateTime.parse(
                URLDecoder.decode(rangeStart, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)
        );
        LocalDateTime endDate = LocalDateTime.parse(
                URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)
        );
        List<Event> events = new ArrayList<>();
        if (sort.toString().equals("EVENT_DATE")) {
            events = eventRepository.findPublicSortByDate(text, text, categories, paid, startDate,
                    endDate, PageRequest.of(from / size, size));
        } else if (sort.toString().equals("VIEWS")) {
            events = eventRepository.findPublicSortByViews(text, text, categories, paid, startDate,
                            endDate, PageRequest.of(from / size, size));
        }

        if (onlyAvailable) {
            events = events.stream()
                    .filter((event -> event.getParticipants().size() < event.getParticipantLimit()))
                    .collect(Collectors.toList());
        }

        List<EventShortDto> eventShortDtoList = events.stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());

        log.info("All necessary events found");
        return eventShortDtoList;
    }

    @Override
    public EventFullDto getEventPublic(long id) {
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.findById(id).orElseThrow());
        log.info("Event with id = {} found", id);
        return eventFullDto;
    }
}
