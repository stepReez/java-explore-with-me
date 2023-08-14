package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.request.UpdateCompilationRequest;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CompilationService;
import ru.practicum.util.mapper.CompilationMapper;

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
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        compilation.setEvents(events);
        Compilation compilationDto = compilationRepository.save(compilation);
        log.info("Compilation with id = {} saved", compilationDto.getId());
        return CompilationMapper.toCompilationDto(compilationDto);
    }

    @Override
    @Transactional
    public void deleteCompilation(long compId) {
        compilationRepository.deleteById(compId);
        log.info("Compilation with id = {} deleted", compId);
    }

    @Override
    @Transactional
    public CompilationDto patchCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(); //todo: exception
        patch(compilation, updateCompilationRequest);
        compilation.setId(compId);
        Compilation newCompilation = compilationRepository.save(compilation);
        log.info("Compilation with id = {} patched", compId);
        return CompilationMapper.toCompilationDto(newCompilation);
    }

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        List<CompilationDto> compilations = compilationRepository.findAll(PageRequest.of(from / size, size))
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
        log.info("Compilations from {} to {} found", from, from + size);
        return compilations;
    }

    @Override
    public CompilationDto getCompilation(long compId) {
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationRepository.findById(compId).orElseThrow());
        log.info("Compilation with id = {} found", compId);
        return compilationDto;
    }

    private void patch(Compilation oldComp, UpdateCompilationRequest newComp) {
        if (newComp.getTitle() != null) {
            oldComp.setTitle(newComp.getTitle());
        }
        if (newComp.getEvents() != null) {
            oldComp.setEvents(eventRepository.findAllById(newComp.getEvents()));
        }
        if (newComp.getPinned() != null) {
            oldComp.setPinned(newComp.getPinned());
        }
    }
}
