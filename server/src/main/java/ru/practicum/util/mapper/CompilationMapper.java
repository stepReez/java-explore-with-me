package ru.practicum.util.mapper;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.request.UpdateCompilationRequest;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.isPinned())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(compilation.getEvents().stream()
                        .map(EventMapper::toShortDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static void patch(Compilation oldComp, UpdateCompilationRequest newComp, List<Event> events) {
        if (newComp.getTitle() != null) {
            oldComp.setTitle(newComp.getTitle());
        }
        if (newComp.getEvents() != null) {
            oldComp.setEvents(events);
        }
        if (newComp.getPinned() != null) {
            oldComp.setPinned(newComp.getPinned());
        }
    }
}
