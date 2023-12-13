package com.example.recipe.services;

import com.example.recipe.converters.RecipeToRecipeCommand;
import com.example.recipe.domain.Recipe;
import com.example.recipe.exceptions.NotFoundException;
import com.example.recipe.repositories.reactive.RecipeReactiveRepository;
import com.example.recipe.services.reactive.RecipeReactiveService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    RecipeReactiveService recipeService;

    @Mock
    RecipeReactiveRepository recipeRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.recipeService = new RecipeReactiveService(this.recipeRepository, recipeToRecipeCommand);
    }

    @Test
    void getRecipes() {

        Recipe recipe = new Recipe();

        when(recipeRepository.findAll()).thenReturn(Flux.just(recipe));

        Iterable<Recipe> recipes = this.recipeService.getRecipes().collectList().block();
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

        when(this.recipeRepository.findAll()).thenReturn(Flux.just());

        Assertions.assertThrows(NotFoundException.class, () -> this.recipeService.getRecipe("1"));

    }
}