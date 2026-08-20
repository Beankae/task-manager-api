package com.NA1.taskmanager.service;

import com.NA1.taskmanager.entity.Category;
import com.NA1.taskmanager.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
    }

    public Category findById(Long id){
        return categoryRepository.findById(id).orElse(null);
    }

    public Category create(Category category){
        return categoryRepository.save(category);
    }
}
