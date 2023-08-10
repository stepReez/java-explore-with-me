package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.model.Location;
import ru.practicum.util.ActionState;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateEventAdminRequest {
    private String annotation;

    private long category;

    private String description;

    private String eventDate;

    private Location location;

    private boolean paid;

    private int participantLimit;

    private boolean requestModeration;

    private ActionState stateAction;

    private String title;
}
