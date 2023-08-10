package ru.practicum.controller.privateController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.RequestService;

import java.util.List;

@Controller
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getParticipationRequest(@PathVariable long userId) {
        return requestService.getParticipationRequest(userId);
    }

    @PostMapping
    public ParticipationRequestDto createParticipationRequest(@PathVariable long userId,
                                                              @RequestParam long eventId) {
        return requestService.createParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable long userId,
                                                              @PathVariable long requestId) {
        return requestService.createParticipationRequest(userId, requestId);
    }
}
