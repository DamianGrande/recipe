package com.example.recipe.services;

import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

import static org.mockito.Mockito.*;

class RecipeServiceImplTest {
    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.recipeService = new RecipeServiceImpl(this.recipeRepository);
    }

    @Test
    void getRecipes() {
        Recipe recipe = new Recipe();
        HashSet<Recipe> recipesData = new HashSet<Recipe>();
        recipesData.add(recipe);
        System.out.println("A");
        when(recipeRepository.findAll()).thenReturn(recipesData);
        System.out.println("B");
        Iterable<Recipe> recipes = this.recipeService.getRecipes();
        int size = 0;
        for (Recipe ignored : recipes)
            size++;
        Assertions.assertEquals(size, 1);
        verify(this.recipeRepository, times(1)).findAll();
    }
}