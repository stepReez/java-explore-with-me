package ru.practicum.service;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.request.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(long compId);

    CompilationDto patchCompilation(long compId, UpdateCompilationRequest updateCompilationRequest);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilation(long compId);
}
