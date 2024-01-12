package com.example.recipe.controllers;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.domain.Recipe;
import com.example.recipe.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.ui.Model;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class RecipeControllerTest extends FluxControllerTest {

    @Test
    void showRecipe() {

        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(this.recipeService.getRecipe(anyString())).thenReturn(Mono.just(recipe));

        EntityExchangeResult<byte[]> response = super.webTestClient.get().uri("/recipe?id=1").exchange().expectStatus().isOk().expectBody().returnResult();

        verify(super.recipeController, times(1)).showRecipe(eq("1"), super.modelArgumentCaptor.capture());

        Model model = super.modelArgumentCaptor.getValue();

        super.checkModelAndView(model, "recipe", "Recipe", response);

    }

    @Test
    void showRecipeForm() {

        EntityExchangeResult<byte[]> response = super.webTestClient.get().uri("/recipe-form").exchange().expectStatus().isOk().expectBody().returnResult();

        verify(super.recipeController, times(1)).showRecipeForm(super.modelArgumentCaptor.capture());

        Model model = super.modelArgumentCaptor.getValue();

        super.checkModelAndView(model, "command", "Recipe Form", response);

    }

    @Test
    void saveOrUpdate() {

        RecipeCommand command = new RecipeCommand();
        command.setId("2");

        when(this.recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(command));

        EntityExchangeResult<byte[]> response = super.webTestClient.post().uri(uriBuilder -> uriBuilder.path("/recipe/save").queryParam("id", "2").queryParam("description", "some string").queryParam("directions", "some directions").build()).contentType(MediaType.APPLICATION_FORM_URLENCODED).exchange().expectStatus().is3xxRedirection().expectBody().returnResult();

        assertTrue(response.toString().contains("Location: [/recipe?id=2]"));

    }

    @Test
    public void testPostNewRecipeFormValidationFail() {

        EntityExchangeResult<byte[]> response = super.webTestClient.post().uri(uriBuilder -> uriBuilder.path("/recipe/save").queryParam("id", "").queryParam("cookTime", "3000").build()).contentType(MediaType.APPLICATION_FORM_URLENCODED).exchange().expectStatus().isOk().expectBody().returnResult();

        ArgumentCaptor<RecipeCommand> recipeCommandArgumentCaptor = ArgumentCaptor.forClass(RecipeCommand.class);

        verify(super.recipeController, times(1)).saveOrUpdate(recipeCommandArgumentCaptor.capture());

        RecipeCommand recipeCommand = recipeCommandArgumentCaptor.getValue();

        assertNotNull(recipeCommand);
        assertEquals("", recipeCommand.getId());
        assertEquals(3000, recipeCommand.getCookTime());
        assertEquals("Recipe Form", super.getViewFromResponse(response));

    }

    @Test
    void testGetUpdateView() {

        RecipeCommand command = new RecipeCommand();
        command.setId("2");

        when(this.recipeService.getCommand(anyString())).thenReturn(Mono.just(command));

        EntityExchangeResult<byte[]> response = super.webTestClient.get().uri("/recipe/1/update").exchange().expectStatus().isOk().expectBody().returnResult();

        verify(super.recipeController, times(1)).updateRecipe(eq("1"), super.modelArgumentCaptor.capture());

        super.checkModelAndView(super.modelArgumentCaptor.getValue(), "command", "Recipe Form", response);

    }

    @Test
    public void testDeleteAction() {

        EntityExchangeResult<byte[]> response = super.webTestClient.get().uri("/recipe/1/delete").exchange().expectStatus().is3xxRedirection().expectBody().returnResult();

        verify(this.recipeService, times(1)).deleteById(anyString());

        assertTrue(response.toString().contains("Location: [/]"));

    }

    @Test
    public void GetRecipeNotFound() {

        when(this.recipeService.getRecipe(anyString())).thenThrow(NotFoundException.class);

        EntityExchangeResult<byte[]> response = super.webTestClient.get().uri("/recipe?id=1").exchange().expectStatus().isNotFound().expectBody().returnResult();

        assertEquals("404 Not Found Error", super.getViewFromResponse(response));

    }

}