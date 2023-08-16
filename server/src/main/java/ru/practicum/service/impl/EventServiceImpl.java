package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.request.*;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.EventService;
import ru.practicum.statClient.client.Client;
import ru.practicum.statDto.dto.ViewStatsDto;
import ru.practicum.util.*;
import ru.practicum.util.mapper.EventMapper;
import ru.practicum.util.mapper.ParticipationRequestMapper;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final ParticipationRepository participationRepository;

    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                        String rangeStart, String rangeEnd, int from, int size) {
        LocalDateTime startDate;
        LocalDateTime endDate;
        if (rangeStart == null || rangeStart.isBlank()) {
            startDate = LocalDateTime.now();
        } else {
            startDate = LocalDateTime.parse(
                    URLDecoder.decode(rangeStart, StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)
            );
        }
        if (rangeEnd == null || rangeEnd.isBlank()) {
            endDate = null;
        } else {
            endDate = LocalDateTime.parse(
                    URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)
            );
        }
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new BadRequestException("End of range cant be before start");
        }
        List<EventFullDto> events = eventRepository.findAdmin(users, states, categories,
                startDate, endDate, PageRequest.of(from / size, size)).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
        log.info("All necessary events found");
        return events;
    }

    @Override
    public EventFullDto patchEvent(long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (event.getEventDate().minusHours(1).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Less than an hour left before the event");
        }
        EventState state = event.getState();
        patch(event, updateEvent);
        if (!state.equals(EventState.PENDING) && event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event not pending");
        }
        if (event.getState().equals(EventState.CANCELED) && state.equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event already published");
        }
        if (updateEvent.getEventDate() != null && LocalDateTime.parse(updateEvent.getEventDate(),
                DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)).isBefore(LocalDateTime.now())) {
            throw new BadRequestException("The start of the event cannot be in the past");
        }
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
                categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() ->
                        new NotFoundException("Category not found")));
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("The date and time for which the event is scheduled cannot be earlier" +
                    " than two hours from the current moment");
        }
        event.setCreated(LocalDateTime.now());
        event.setInitiator(userRepository.findById(userId).orElseThrow());
        event.setState(EventState.PENDING);
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        Event eventFullDto = eventRepository.save(event);
        log.info("Event with id = {} saved", eventFullDto.getId());
        return EventMapper.toEventFullDto(eventFullDto);
    }

    @Override
    public EventFullDto getEventByUser(long userId, long eventId) {
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id = %d found", eventId))));
        log.info("Event with id = {} found", eventId);
        return eventFullDto;
    }

    @Override
    public EventFullDto patchEventByUser(long userId, long eventId, UpdateEventUserRequest userRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event already published");
        }
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ConflictException("The date and time for which the event is scheduled cannot be earlier" +
                    " than two hours from the current moment");
        }
        if (userRequest.getEventDate() != null && LocalDateTime.parse(userRequest.getEventDate(),
                DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)).isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("The start of the event cannot be in the past");
        }
        patch(event, userRequest);
        Event newEvent = eventRepository.save(event);
        log.info("Event with id = {} saved", eventId);
        return EventMapper.toEventFullDto(newEvent);
    }

    @Override
    public List<ParticipationRequestDto> getParticipation(long userId, long eventId) {
        List<ParticipationRequestDto> requests = participationRepository.findAllByUsersEvent(eventId)
                .stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        log.info("Participations for event {} found", eventId);
        return requests;
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult patchParticipation(long userId, long eventId,
                                                             EventRequestStatusUpdateRequest statusUpdateRequest) {
        List<Long> ids = statusUpdateRequest.getRequestIds();
        ids.forEach(x -> {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
            ParticipationRequest request = participationRepository.findById(x).orElseThrow(() ->
                    new NotFoundException("Request not found"));
            if (event.getParticipants().size() >= event.getParticipantLimit()) {
                throw new ConflictException("Event participant limit reached");
            }
            if (!participationRepository.findById(x).orElseThrow(() -> new NotFoundException("Request not found"))
                    .getStatus().equals("PENDING")) {
                throw new ConflictException("Request status must be PENDING");
            }
            request.setStatus(statusUpdateRequest.getStatus().toString());
            participationRepository.save(request);
//            if (statusUpdateRequest.getStatus().toString().equals("CONFIRMED")) {
//                List<User> users = event.getParticipants();
//                users.add(request.getRequester());
//                event.setParticipants(users);
//                eventRepository.save(event);
//            }
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
    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid, String rangeStart,
                                               String rangeEnd, boolean onlyAvailable, EventsSort sort,
                                               int from, int size) {
        LocalDateTime startDate;
        LocalDateTime endDate;
        if (rangeStart == null || rangeStart.isBlank()) {
            startDate = LocalDateTime.now().minusMinutes(10);
        } else {
            startDate = LocalDateTime.parse(
                    URLDecoder.decode(rangeStart, StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)
            );
        }
        if (rangeEnd == null || rangeEnd.isBlank()) {
            endDate = null;
        } else {
            endDate = LocalDateTime.parse(
                    URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)
            );
        }
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new BadRequestException("End of range cant be before start");
        }
        List<Event> events = new ArrayList<>();
        if (sort != null && sort.toString().equals("EVENT_DATE")) {
            events = eventRepository.findPublicSortByDate(text, categories, paid, startDate,
                    endDate, PageRequest.of(from / size, size));
        } else if (sort != null && sort.toString().equals("VIEWS")) {
            events = eventRepository.findPublicSortByViews(text, categories, paid, startDate,
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

        int arrSize = eventShortDtoList.size();
        for (int i = 0; i < size - arrSize; i++) {
            eventShortDtoList.add(null);
        }
        log.info("All necessary events found");
        return eventShortDtoList;
    }

    @Override
    public EventFullDto getEventPublic(long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty() || !event.get().getState().equals(EventState.PUBLISHED)) {
           throw new NotFoundException(String.format("Event with id = %d found", id));
        }
        Event eventView = event.get();
        ArrayList<String> uri = new ArrayList<>();
        uri.add("/events/" + id);
        long views = getViews(uri);
        eventView.setViews(views);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(eventView));
        log.info("Event with id = {} found", id);
        return eventFullDto;
    }

    private void patch(Event event, UpdateEventUserRequest userRequest) {
        Optional.ofNullable(userRequest.getEventDate()).ifPresent(eventDate -> event.setEventDate(LocalDateTime.parse(
                eventDate,
                DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)
        )));
        Optional.ofNullable(userRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(userRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(userRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(userRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(userRequest.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(userRequest.getCategory()).ifPresent(categoryId ->
                event.setCategory(categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Category notFound"))));
        Optional.ofNullable(userRequest.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(userRequest.getStateAction()).ifPresent(actionState -> {
            if (actionState.equals(StateActionUser.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else {
                event.setState(EventState.CANCELED);
            }
        });
        Optional.ofNullable(userRequest.getLocation()).ifPresent(event::setLocation);
    }

    private void patch(Event event, UpdateEventAdminRequest userRequest) {
        Optional.ofNullable(userRequest.getEventDate()).ifPresent(eventDate -> event.setEventDate(LocalDateTime.parse(
                eventDate,
                DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)
        )));
        Optional.ofNullable(userRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(userRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(userRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(userRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(userRequest.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(userRequest.getCategory()).ifPresent(categoryId ->
                event.setCategory(categoryRepository.findById(categoryId).orElseThrow(() ->
                        new NotFoundException("Category notFound"))));
        Optional.ofNullable(userRequest.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(userRequest.getStateAction()).ifPresent(actionState -> {
            if (actionState.equals(StateActionAdmin.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
            } else {
                event.setState(EventState.CANCELED);
            }
        });
        Optional.ofNullable(userRequest.getLocation()).ifPresent(event::setLocation);
    }

    private long getViews(ArrayList<String> uris) {
        Client client = new Client(new RestTemplate());
        List<ViewStatsDto> viewStats = client.getStats(LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100), uris, true);
        log.info(viewStats.toString());
        return viewStats.size();
    }
}
