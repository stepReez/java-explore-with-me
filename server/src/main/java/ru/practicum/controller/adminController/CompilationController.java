package ru.practicum.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.request.UpdateCompilationRequest;

@Controller
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class CompilationController {

    @PostMapping
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        return null;
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable long compId) {

    }

    @PatchMapping("/{compId}")
    public CompilationDto patchCompilation(@PathVariable long compId,
                                           @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return null;
    }
}
