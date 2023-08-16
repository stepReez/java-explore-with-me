package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NewEventDto {
    @NotBlank
    @NotNull
    @Size(min = 20, max = 2000)
    private String annotation;

    private long category;

    @NotBlank
    @NotNull
    @Size(min = 20, max = 7000)
    private String description;

    private String eventDate;

    private Location location;

    private boolean paid;

    private int participantLimit;

    private Boolean requestModeration;

    @NotBlank
    @NotNull
    @Size(min = 3, max = 120)
    private String title;
}
