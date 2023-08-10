package ru.practicum.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.request.UpdateCompilationRequest;
import ru.practicum.service.CompilationService;

import java.util.List;

@Service
public class CompilationServiceImpl implements CompilationService {
    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        return null;
    }

    @Override
    public void deleteCompilation(long compId) {

    }

    @Override
    public CompilationDto patchCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        return null;
    }

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        return null;
    }

    @Override
    public CompilationDto getCompilation(long compId) {
        return null;
    }
}
