package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.model.Location;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NewEventDto {
    private String annotation;

    private long category;

    private String description;

    private String eventDate;

    private Location location;

    private boolean paid;

    private int participantLimit;

    private boolean requestModeration;

    private String title;
}
