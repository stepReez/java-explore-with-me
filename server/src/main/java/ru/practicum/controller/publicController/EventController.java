package ru.practicum.controller.publicController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.util.EventsSort;

import java.util.List;

@Controller
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventController {

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam String text,
                                         @RequestParam List<Long> categories,
                                         @RequestParam boolean paid,
                                         @RequestParam String rangeStart,
                                         @RequestParam String rangeEnd,
                                         @RequestParam boolean onlyAvailable,
                                         @RequestParam EventsSort sort,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        return null;
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable long id ) {
        return null;
    }
}