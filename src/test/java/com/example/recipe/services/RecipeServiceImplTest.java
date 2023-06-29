package com.example.recipe.services;

import com.example.recipe.converters.RecipeCommandToRecipe;
import com.example.recipe.converters.RecipeToRecipeCommand;
import com.example.recipe.domain.Recipe;
import com.example.recipe.exceptions.NotFoundException;
import com.example.recipe.repositories.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.LinkedList;

import static org.mockito.Mockito.*;

class RecipeServiceImplTest {
    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.recipeService = new RecipeServiceImpl(this.recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
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

    @Test
    public void deleteById() {
        String idToDelete = "2";
        this.recipeService.deleteById(idToDelete);
        verify(this.recipeRepository, times(1)).deleteById(anyString());
    }

    @Test
    public void getRecipeByIdNotFound() {
        when(this.recipeRepository.findAll()).thenReturn(new LinkedList<>());
        Assertions.assertThrows(NotFoundException.class, () -> this.recipeService.getRecipe("1"));
    }
}