package com.example.recipe.controllers;

import com.example.recipe.commands.IngredientCommand;
import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class IngredientControllerTest extends FluxControllerTest {

    @Test
    void listIngredients() {

        RecipeCommand recipeCommand = new RecipeCommand();

        when(this.recipeService.getCommand(anyString())).thenReturn(Mono.just(recipeCommand));

        EntityExchangeResult<byte[]> response = super.webTestClient.get().uri("/recipe/1/ingredients").exchange().expectStatus().isOk().expectBody().returnResult();

        verify(this.recipeService, times(1)).getCommand(anyString());
        verify(super.ingredientController, times(1)).listIngredients(eq("1"), super.modelArgumentCaptor.capture());

        super.checkModelAndView(super.modelArgumentCaptor.getValue(), "recipe", "Ingredient List", response);

    }

    @Test
    void showIngredient() {

        IngredientCommand ingredientCommand = new IngredientCommand();

        when(super.ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(Mono.just(ingredientCommand));

        EntityExchangeResult<byte[]> response = super.webTestClient.get().uri("/recipe/1/ingredient/2/show").exchange().expectStatus().isOk().expectBody().returnResult();

        verify(super.ingredientController, times(1)).showIngredient(eq("1"), eq("2"), super.modelArgumentCaptor.capture());

        super.checkModelAndView(super.modelArgumentCaptor.getValue(), "ingredient", "Ingredient Detail", response);

    }

    @Test
    public void updateIngredientForm() {

        IngredientCommand ingredientCommand = new IngredientCommand();

        when(super.ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(Mono.just(ingredientCommand));
        when(super.unitOfMeasureService.listAllUoms()).thenReturn(Flux.just());

        EntityExchangeResult<byte[]> response = super.webTestClient.get().uri("/recipe/1/ingredient/2/update").exchange().expectStatus().isOk().expectBody().returnResult();

        verify(super.ingredientController, times(1)).updateIngredient(eq("1"), eq("2"), super.modelArgumentCaptor.capture());

        Model model = super.modelArgumentCaptor.getValue();

        assertNotNull(model);
        assertNotNull(model.getAttribute("ingredient"));
        assertNotNull(model.getAttribute("uomList"));
        assertEquals("Ingredient Form", super.getViewFromResponse(response));

    }

    @Test
    public void saveOrUpdate() {

        when(this.unitOfMeasureService.findById("unitId")).thenReturn(Mono.just(new UnitOfMeasure()));

        EntityExchangeResult<byte[]> response = super.webTestClient.post().uri(uriBuilder -> uriBuilder.path("/recipe/2/ingredient").queryParam("id", "3").queryParam("description", "some string").queryParam("uomId", "unitId").build()).contentType(MediaType.APPLICATION_FORM_URLENCODED).exchange().expectStatus().is3xxRedirection().expectBody().returnResult();

        assertTrue(response.toString().contains("Location: [/recipe/2/ingredient/3/show]"));

    }

    @Test
    public void newIngredientForm() {

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("69");

        when(this.recipeService.getCommand(anyString())).thenReturn(Mono.just(recipeCommand));
        when(this.unitOfMeasureService.listAllUoms()).thenReturn(Flux.just());

        EntityExchangeResult<byte[]> response = super.webTestClient.get().uri("/recipe/69/ingredient/new").exchange().expectStatus().isOk().expectBody().returnResult();

        verify(this.recipeService, times(0)).getCommand(anyString());
        verify(super.ingredientController, times(1)).newRecipeForm(eq("69"), super.modelArgumentCaptor.capture());

        Model model = super.modelArgumentCaptor.getValue();

        assertNotNull(model);
        assertNotNull(model.getAttribute("uomList"));

        IngredientCommand ingredient = (IngredientCommand) model.getAttribute("ingredient");

        assertNotNull(ingredient);
        assertEquals("69", ingredient.getRecipeId());

    }

    @Test
    public void deleteIngredient() {

        EntityExchangeResult<byte[]> response = super.webTestClient.get().uri("/recipe/1/ingredient/3/delete").exchange().expectStatus().is3xxRedirection().expectBody().returnResult();

        verify(this.ingredientService, times(1)).deleteIngredient("1", "3");

        assertTrue(response.toString().contains("Location: [/recipe/1/ingredients]"));

    }

}