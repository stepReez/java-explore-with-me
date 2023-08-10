package ru.practicum.controller.privateController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.UpdateEventUserRequest;
import ru.practicum.service.EventService;

import java.util.List;

@Controller
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEvents(@PathVariable long userId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventsByUser(userId, from, size);
    }

    @PostMapping
    public EventFullDto createEvent(@PathVariable long userId,
                                    @RequestBody NewEventDto newEventDto) {
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable long userId,
                                 @PathVariable long eventId) {
        return eventService.getEventByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(@PathVariable long userId,
                                   @PathVariable long eventId,
                                   @RequestBody UpdateEventUserRequest userRequest) {
        return eventService.patchEventByUser(userId, eventId, userRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipation(@PathVariable long userId,
                                                          @PathVariable long eventId) {
        return eventService.getParticipation(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult patchParticipation(@PathVariable long userId,
                                                             @PathVariable long eventId,
                                                             @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {
        return eventService.patchParticipation(userId, eventId, statusUpdateRequest);
    }
}
