package ru.practicum.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;

@Controller
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class CategoryController {

    @PostMapping
    public CategoryDto createCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return null;
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable long catId) {
    }

    @PatchMapping("/{catId}")
    public CategoryDto patchCategory(@PathVariable long catId, @RequestBody CategoryDto categoryDto) {
        return null;
    }
}
