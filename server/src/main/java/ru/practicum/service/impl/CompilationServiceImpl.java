package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.request.UpdateCompilationRequest;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CompilationService;
import ru.practicum.util.mapper.CompilationMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            events = eventRepository.findAllById(newCompilationDto.getEvents());
        }
        compilation.setEvents(events);
        Compilation compilationDto = compilationRepository.save(compilation);
        log.info("Compilation with id = {} saved", compilationDto.getId());
        return CompilationMapper.toCompilationDto(compilationDto);
    }

    @Override
    public void deleteCompilation(long compId) {
        log.info("Try to delete compilation {}", compId);
        if (compilationRepository.findById(compId).isEmpty()) {
            throw new NotFoundException(String.format("Compilation with id = %d not found", compId));
        }
        compilationRepository.deleteById(compId);
        log.info("Compilation with id = {} deleted", compId);
    }

    @Override
    public CompilationDto patchCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        log.info("Try to get compilation {}", compId);
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Compilation with id = %d not found", compId)));
        List<Event> events = null;
            if (updateCompilationRequest.getEvents() != null) {
                events = eventRepository.findAllById(updateCompilationRequest.getEvents());
            }
        CompilationMapper.patch(compilation, updateCompilationRequest, events);
        compilation.setId(compId);
        Compilation newCompilation = compilationRepository.save(compilation);
        log.info("Compilation with id = {} patched", compId);
        return CompilationMapper.toCompilationDto(newCompilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(PageRequest.of(from / size, size))
                    .stream().collect(Collectors.toList());
        } else {
            compilations = new ArrayList<>(compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size)));
        }
        log.info("Compilations from {} to {} found", from, from + size);
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(long compId) {
        log.info("Try to get compilation {}", compId);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id = %d not found", compId))));
        log.info("Compilation with id = {} found", compId);
        return compilationDto;
    }
}
