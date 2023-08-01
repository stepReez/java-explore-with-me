package ru.practicum.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

public interface StatService {
    HitDto hitRequest(HitDto hitDto);

    List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique);
}
