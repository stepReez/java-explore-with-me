package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CategoryService;
import ru.practicum.util.mapper.CategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        if (categoryRepository.findAll().stream()
                .anyMatch((category) -> category.getName().equals(newCategoryDto.getName()))) {
            throw new ConflictException("Category with this name already exist");
        }
        Category category = categoryRepository.save(CategoryMapper.toCategory(newCategoryDto));
        log.info("Category with id = {} saved", category.getId());
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategory(long catId) {
        if (categoryRepository.findById(catId).isEmpty()) {
            throw new NotFoundException("Category not found");
        }
        if (!eventRepository.findByCategory(catId, Pageable.ofSize(1)).isEmpty()) {
            throw new ConflictException("Category associated with the event");
        }
        categoryRepository.deleteById(catId);
        log.info("Category with id = {} deleted", catId);
    }

    @Override
    public CategoryDto patchCategory(long catId, CategoryDto categoryDto) {
        if (categoryRepository.findAll().stream()
                .anyMatch(category -> (category.getName().equals(categoryDto.getName())) &&
                (category.getId() != catId))) {
            throw new ConflictException("Category with this name already exist");
        }
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException(String.format("Category with id = %d not found", catId)));
        category.setName(categoryDto.getName());
        category.setId(catId);
        Category newCategory = categoryRepository.save(category);
        log.info("Category with id = {} patched", catId);
        return CategoryMapper.toCategoryDto(newCategory);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        List<CategoryDto> categories = categoryRepository.findAll(PageRequest.of(from / size, size)).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
        log.info("Categories from {} to {} found", from, from + size);
        return categories;
    }

    @Override
    public CategoryDto getCategory(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id = %d found", catId)));
        log.info("Category with id = {} found", catId);
        return CategoryMapper.toCategoryDto(category);
    }
}
