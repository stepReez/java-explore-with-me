package ru.practicum.statService.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.statDto.dto.HitDto;
import ru.practicum.statDto.dto.ViewStatsDto;
import ru.practicum.statService.service.StatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatController {

    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto hitRequest(@RequestBody HitDto hitDto) {
        return service.hitRequest(hitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam String start,
                                      @RequestParam String end,
                                      @RequestParam (defaultValue = "") List<String> uris,
                                      @RequestParam (defaultValue = "false") boolean unique) {
        return service.getStats(start, end, uris, unique);
    }
}
