package ru.practicum.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.request.UpdateEventAdminRequest;
import ru.practicum.service.EventService;
import ru.practicum.util.EventState;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam List<Long> users,
                                        @RequestParam List<EventState> states,
                                        @RequestParam List<Long> categories,
                                        @RequestParam String rangeStart,
                                        @RequestParam String rangeEnd,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        if (rangeStart == null || rangeStart.isBlank()) {
            rangeStart = URLEncoder.encode(String.valueOf(LocalDateTime.now()), StandardCharsets.UTF_8);
        }
        if (rangeEnd == null || rangeEnd.isBlank()) {
            rangeEnd = URLEncoder.encode(String.valueOf(LocalDateTime.MAX), StandardCharsets.UTF_8);
        }
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(@PathVariable long eventId, @RequestBody Map<Object, Object> updateEvent) {
        return eventService.patchEvent(eventId, updateEvent);
    }
}
