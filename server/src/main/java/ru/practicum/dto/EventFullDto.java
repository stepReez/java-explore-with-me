package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.model.Location;
import ru.practicum.util.EventState;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EventFullDto {
    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    private String createdOn;

    private String description;

    private String eventDate;

    private long id;

    private UserShortDto initiator;

    private Location location;

    private boolean paid;

    private int participantLimit;

    private String publishedOn;

    private boolean requestModeration;

    private EventState state;

    private String title;

    private long views;

    private List<CommentDto> comments;
}
