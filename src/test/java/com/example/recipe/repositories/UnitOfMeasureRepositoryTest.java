package com.example.recipe.repositories;

import com.example.recipe.bootstrap.DataLoader;
import com.example.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
class UnitOfMeasureRepositoryTest {

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RecipeRepository recipeRepository;


    @BeforeEach
    void setUp() throws Exception {
        this.unitOfMeasureRepository.deleteAll();
        this.categoryRepository.deleteAll();
        this.recipeRepository.deleteAll();
        new DataLoader(this.unitOfMeasureRepository, this.categoryRepository, this.recipeRepository).run("");
    }

    @Test
    void findByDescription() {
        Optional<UnitOfMeasure> uomOptional = this.unitOfMeasureRepository.findByDescription("Teaspoon");
        Assertions.assertEquals("Teaspoon", uomOptional.get().getDescription());
    }

    @Test
    void findByDescriptionCup() {
        Optional<UnitOfMeasure> uomOptional = this.unitOfMeasureRepository.findByDescription("Cup");
        Assertions.assertEquals("Cup", uomOptional.get().getDescription());
    }
}