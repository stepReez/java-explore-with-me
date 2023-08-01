package ru.practicum.statDto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HitDto {
    private long id;

    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
