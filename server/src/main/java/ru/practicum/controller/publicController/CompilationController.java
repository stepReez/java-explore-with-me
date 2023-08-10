package ru.practicum.controller.publicController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.CompilationDto;

import java.util.List;

@Controller
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class CompilationController {

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return null;
    }

    @GetMapping("/compId")
    public CompilationDto getCompilation(@PathVariable long compId) {
        return null;
    }
}
