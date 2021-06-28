package com.example.recipe.controllers;

import com.example.recipe.domain.Category;
import com.example.recipe.domain.UnitOfMeasure;
import com.example.recipe.repositories.CategoryRepository;
import com.example.recipe.repositories.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class IndexController {
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    public IndexController(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @RequestMapping({"", "/", "/index"})
    public String getIndexPage() {
        Optional<Category> categoryOptional = this.categoryRepository.findByDescription("American");
        Optional<UnitOfMeasure> unitOfMeasureOptional = this.unitOfMeasureRepository.findByDescription("Teaspoon");
        System.out.println("Cat id is: " + categoryOptional.get().getId());
        System.out.println("UOM id is: " + unitOfMeasureOptional.get().getId());
        return "index";
    }
}
