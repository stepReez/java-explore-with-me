package ru.practicum.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.request.UpdateEventAdminRequest;
import ru.practicum.util.EventState;

import java.util.List;

@Controller
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class EventController {

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam List<Long> users,
                                        @RequestParam List<EventState> states,
                                        @RequestParam List<Long> categories,
                                        @RequestParam String rangeStart,
                                        @RequestParam String rangeEnd,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return null;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(@PathVariable long eventId, @RequestBody UpdateEventAdminRequest updateEvent) {
        return null;
    }
}
