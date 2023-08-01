package ru.practicum.util;


import ru.practicum.dto.HitDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {
    public static Hit toHit(HitDto hitDto) {
        return Hit.builder()
                .id(hitDto.getId())
                .uri(hitDto.getUri())
                .app(hitDto.getApp())
                .timeStamp(LocalDateTime.parse(
                        hitDto.getTimestamp(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ))
                .ip(hitDto.getIp())
                .build();
    }

    public static HitDto toHitDto(Hit hit) {
        return HitDto.builder()
                .id(hit.getId())
                .uri(hit.getUri())
                .app(hit.getApp())
                .timestamp(hit.getTimeStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .ip(hit.getIp())
                .build();
    }
}
