package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.service.EventService;
import ru.practicum.statClient.client.Client;
import ru.practicum.util.EventsSort;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventPublicController {

    @Autowired
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(defaultValue = "false") boolean paid,
                                         @RequestParam(defaultValue = "") String rangeStart,
                                         @RequestParam(defaultValue = "") String rangeEnd,
                                         @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                         @RequestParam(defaultValue = "EVENT_DATE") EventsSort sort,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        Client client = new Client(new RestTemplate());
        client.postHit("GetEvents", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        log.info(request.getRequestURI());
        log.info(request.getRemoteAddr());
        return eventService.getEventsPublic(text, categories, paid, rangeStart, rangeEnd,
                                            onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable long id, HttpServletRequest request) {
        Client client = new Client(new RestTemplate());
        client.postHit("GetEvent", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        log.info(request.getRequestURI());
        log.info(request.getRemoteAddr());
        return eventService.getEventPublic(id);
    }
}
