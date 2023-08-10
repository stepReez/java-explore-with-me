package ru.practicum.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return null;
    }

    @Override
    public void deleteCategory(long catId) {

    }

    @Override
    public CategoryDto patchCategory(long catId, CategoryDto categoryDto) {
        return null;
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        return null;
    }

    @Override
    public CategoryDto getCategory(long catId) {
        return null;
    }
}
