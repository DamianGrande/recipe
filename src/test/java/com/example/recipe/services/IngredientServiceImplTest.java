package com.example.recipe.services;

import com.example.recipe.commands.IngredientCommand;
import com.example.recipe.converters.IngredientCommandToIngredient;
import com.example.recipe.converters.IngredientToIngredientCommand;
import com.example.recipe.domain.Ingredient;
import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.IngredientRepository;
import com.example.recipe.repositories.RecipeRepository;
import com.example.recipe.repositories.UnitOfMeasureRepository;
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
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Mock
    IngredientRepository ingredientRepository;

    IngredientService ingredientService;

    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand();
        this.ingredientCommandToIngredient = new IngredientCommandToIngredient();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.ingredientService = new IngredientServiceImpl(this.ingredientToIngredientCommand, ingredientCommandToIngredient, this.recipeRepository, unitOfMeasureRepository, ingredientRepository);
    }

    @Test
    void findByRecipeIdAndIngredientId() {
    }

    @Test
    void findByRecipeIdAndIngredientIdHappyPath() {
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("1");
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("2");
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("3");
        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        Optional<Recipe> recipeOptional = Optional.of(recipe);
        when(this.recipeRepository.findById(anyString())).thenReturn(recipeOptional);
        IngredientCommand ingredientCommand = this.ingredientService.findByRecipeIdAndIngredientId("1", "3");
        assertEquals("3", ingredientCommand.getId());
        verify(this.recipeRepository, times(1)).findById(anyString());
    }

    @Test
    public void saveIngredientCommand() {
        Recipe recipe = new Recipe();
        recipe.setId("2");
        IngredientCommand command = new IngredientCommand();
        command.setId("3");
        command.setRecipeId("2");
        Optional<Recipe> recipeOptional = Optional.of(new Recipe());
        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId("3");
        when(this.recipeRepository.findById(anyString())).thenReturn(recipeOptional);
        when(this.recipeRepository.save(any())).thenReturn(savedRecipe);
        when(this.ingredientRepository.save(any())).thenReturn(this.ingredientCommandToIngredient.convert(command));
        IngredientCommand savedCommand = this.ingredientService.saveIngredientCommand(command);
        assertEquals("3", savedCommand.getId());
        verify(this.recipeRepository, times(1)).findById(anyString());
        verify(this.recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    public void deleteIngredient() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient = new Ingredient();
        ingredient.setId("3");
        recipe.addIngredient(ingredient);
        when(this.recipeRepository.findById("1")).thenReturn(Optional.of(recipe));
        this.ingredientService.deleteIngredient("1", "3");
        verify(this.ingredientRepository, times(1)).deleteById("3");
        assertTrue(recipe.getIngredients().isEmpty());
        verify(this.recipeRepository, times(1)).save(recipe);
    }

    @Test
    public void deleteIngredientNotExistentRecipe() throws Exception {
        when(this.recipeRepository.findById(anyString())).thenReturn(null);
        assertThrows(Exception.class, () -> this.ingredientService.deleteIngredient("1", "3"));
    }

    @Test
    public void deleteIngredientNotExistentIngredient() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient = new Ingredient();
        ingredient.setId("3");
        recipe.addIngredient(ingredient);
        when(this.recipeRepository.findById("1")).thenReturn(Optional.of(recipe));
        assertThrows(Exception.class, () -> this.ingredientService.deleteIngredient("1", "2"));
    }
}