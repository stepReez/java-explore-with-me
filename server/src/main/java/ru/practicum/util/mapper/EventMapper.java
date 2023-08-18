package ru.practicum.util.mapper;

import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.request.UpdateEventAdminRequest;
import ru.practicum.dto.request.UpdateEventUserRequest;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.util.Constants;
import ru.practicum.util.EventState;
import ru.practicum.util.StateActionAdmin;
import ru.practicum.util.StateActionUser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class EventMapper {

    public static EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getParticipants().size())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)))
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto, Category category) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(
                        newEventDto.getEventDate(),
                        DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)
                ))
                .location(newEventDto.getLocation())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreated().format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)))
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getCreated().format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)))
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .confirmedRequests(event.getParticipants().size())
                .build();

    }

    public static void patch(Event event, UpdateEventUserRequest userRequest, Category category) {
        patchBody(event, userRequest.getEventDate(), userRequest.getPaid(), userRequest.getDescription(),
                userRequest.getParticipantLimit(), userRequest.getAnnotation(), userRequest.getTitle(), category,
                userRequest.getRequestModeration(), userRequest.getLocation());
        Optional.ofNullable(userRequest.getStateAction()).ifPresent(actionState -> {
            if (actionState.equals(StateActionUser.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else {
                event.setState(EventState.CANCELED);
            }
        });
        Optional.ofNullable(userRequest.getLocation()).ifPresent(event::setLocation);
    }

    public static void patch(Event event, UpdateEventAdminRequest userRequest, Category category) {
        patchBody(event, userRequest.getEventDate(), userRequest.getPaid(), userRequest.getDescription(),
                userRequest.getParticipantLimit(), userRequest.getAnnotation(), userRequest.getTitle(), category,
                userRequest.getRequestModeration(), userRequest.getLocation());
        Optional.ofNullable(userRequest.getStateAction()).ifPresent(actionState -> {
            if (actionState.equals(StateActionAdmin.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
            } else {
                event.setState(EventState.CANCELED);
            }
        });
    }

    private static void patchBody(Event event, String eventDate, Boolean paid, String description,
                                  Integer participantLimit, String annotation, String title, Category category,
                                  Boolean requestModeration, Location location) {
        Optional.ofNullable(eventDate).ifPresent(e -> event.setEventDate(LocalDateTime.parse(
                e,
                DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)
        )));
        Optional.ofNullable(paid).ifPresent(event::setPaid);
        Optional.ofNullable(description).ifPresent(event::setDescription);
        Optional.ofNullable(participantLimit).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(annotation).ifPresent(event::setAnnotation);
        Optional.ofNullable(title).ifPresent(event::setTitle);
        Optional.ofNullable(category).ifPresent(event::setCategory);
        Optional.ofNullable(requestModeration).ifPresent(event::setRequestModeration);
        Optional.ofNullable(location).ifPresent(event::setLocation);
    }
}
