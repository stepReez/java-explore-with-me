package ru.practicum.statService.service;

import ru.practicum.statDto.dto.HitDto;
import ru.practicum.statDto.dto.ViewStatsDto;

import java.util.List;

public interface StatService {
    HitDto hitRequest(HitDto hitDto);

    List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique);
}
