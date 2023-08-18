package ru.practicum.dto;

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
public class EventShortDto {
    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    private String eventDate;

    private long id;

    private UserShortDto initiator;

    private boolean paid;

    private String title;

    private long views;
}
