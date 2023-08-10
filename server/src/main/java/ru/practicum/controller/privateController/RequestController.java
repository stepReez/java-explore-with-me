package ru.practicum.controller.privateController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

@Controller
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {

    @GetMapping
    public List<ParticipationRequestDto> getParticipationRequest(@PathVariable long userId) {
        return null;
    }

    @PostMapping
    public ParticipationRequestDto createParticipationRequest(@PathVariable long userId,
                                                              @RequestParam long eventId) {
        return null;
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable long userId,
                                                              @PathVariable long requestId) {
        return null;
    }
}
