package com.example.recipe.services;

import com.example.recipe.commands.IngredientCommand;
import com.example.recipe.converters.IngredientCommandToIngredient;
import com.example.recipe.converters.IngredientToIngredientCommand;
import com.example.recipe.domain.Ingredient;
import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.reactive.IngredientReactiveRepository;
import com.example.recipe.repositories.reactive.RecipeReactiveRepository;
import com.example.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import com.example.recipe.services.reactive.IngredientReactiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Mock
    RecipeReactiveRepository recipeRepository;

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    @Mock
    IngredientReactiveRepository ingredientRepository;

    IngredientService ingredientService;

    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand();
        this.ingredientCommandToIngredient = new IngredientCommandToIngredient();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.ingredientService = new IngredientReactiveService(this.ingredientToIngredientCommand, ingredientCommandToIngredient, this.recipeRepository, unitOfMeasureRepository, ingredientRepository);
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
        when(this.recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        IngredientCommand ingredientCommand = this.ingredientService.findByRecipeIdAndIngredientId("1", "3").block();
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
        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId("3");
        when(this.recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(this.recipeRepository.save(any())).thenReturn(Mono.just(savedRecipe));
        when(this.ingredientRepository.save(any())).thenReturn(Mono.just(this.ingredientCommandToIngredient.convert(command)));
        verify(this.recipeRepository, times(1)).findById(anyString());
        verify(this.recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    public void deleteIngredient() {

        Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient = new Ingredient();
        ingredient.setId("3");
        recipe.addIngredient(ingredient);

        when(this.recipeRepository.findById("1")).thenReturn(Mono.just(recipe));
        when(this.ingredientRepository.deleteById("3")).thenReturn(Mono.empty());
        when(this.recipeRepository.save(recipe)).thenReturn(Mono.just(recipe));

        this.ingredientService.deleteIngredient("1", "3");

        verify(this.ingredientRepository, times(1)).deleteById("3");
        assertTrue(recipe.getIngredients().isEmpty());
        verify(this.recipeRepository, times(1)).save(recipe);

    }

    @Test
    public void deleteIngredientNotExistentRecipe() {
        when(this.recipeRepository.findById(anyString())).thenReturn(null);
        assertThrows(Exception.class, () -> this.ingredientService.deleteIngredient("1", "3"));
    }

    @Test
    public void deleteIngredientNotExistentIngredient() {
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient = new Ingredient();
        ingredient.setId("3");
        recipe.addIngredient(ingredient);
        when(this.recipeRepository.findById("1")).thenReturn(Mono.just(recipe));
        assertThrows(Exception.class, () -> this.ingredientService.deleteIngredient("1", "2"));
    }
}