package com.honestbite.www.category.service;

import com.honestbite.www.category.dto.CategoryDTO;
import com.honestbite.www.category.persistence.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDTO.GetOutput> fetchAllCategories() {
        return categoryRepository.findAll().stream()
                .map(entity -> CategoryDTO.GetOutput.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .build())
                .collect(Collectors.toList());
    }
}