package com.example.recipe.services;

import com.example.recipe.commands.IngredientCommand;
import com.example.recipe.converters.IngredientToIngredientCommand;
import com.example.recipe.domain.Ingredient;
import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    @Mock
    RecipeRepository recipeRepository;

    IngredientService ingredientService;

    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.ingredientService = new IngredientServiceImpl(this.ingredientToIngredientCommand, this.recipeRepository);
    }

    @Test
    void findByRecipeIdAndIngredientId() {
    }

    @Test
    void findByRecipeIdAndIngredientIdHappyPath() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(3L);
        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        Optional<Recipe> recipeOptional = Optional.of(recipe);
        when(this.recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        IngredientCommand ingredientCommand = this.ingredientService.findByRecipeIdAndIngredientId(1L, 3L);
        assertEquals(3L, ingredientCommand.getId());
        assertEquals(1L, ingredientCommand.getRecipe().getId());
        verify(this.recipeRepository, times(1)).findById(anyLong());
    }
}