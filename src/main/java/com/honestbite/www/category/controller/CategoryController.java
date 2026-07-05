package com.honestbite.www.category.controller;

import com.honestbite.www.category.dto.CategoryDTO;
import com.honestbite.www.category.model.CategoryEntity;
import com.honestbite.www.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO.GetOutput>> getAllCategories(){
        return ResponseEntity.ok(categoryService.fetchAllCategories());
    }
}
