package com.example.recipe.controllers;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.domain.Recipe;
import com.example.recipe.exceptions.NotFoundException;
import com.example.recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecipeControllerTest {
    @Mock
    RecipeService recipeService;

    RecipeController controller;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.controller = new RecipeController(this.recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
    }

    @Test
    void showRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        when(this.recipeService.getRecipe(anyLong())).thenReturn(recipe);
        this.mockMvc.perform(get("/recipe?id=1")).andExpect(status().isOk()).andExpect(view().name("recipe-detail")).andExpect(model().attributeExists("recipe"));
    }

    @Test
    void showRecipeForm() throws Exception {
        this.mockMvc.perform(get("/recipe-form")).andExpect(status().isOk()).andExpect(view().name("form")).andExpect(model().attributeExists("command"));
    }

    @Test
    void saveOrUpdate() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(2L);
        when(this.recipeService.saveRecipeCommand(any())).thenReturn(command);
        this.mockMvc.perform(post("/recipe/save").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("id", "").param("description", "some string")).andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/recipe?id=2"));
    }

    @Test
    void testGetUpdateView() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(2L);
        when(this.recipeService.getCommand(anyLong())).thenReturn(command);
        this.mockMvc.perform(get("/recipe/1/update")).andExpect(status().isOk()).andExpect(view().name("form")).andExpect(model().attributeExists("command"));
    }

    @Test
    public void testDeleteAction() throws Exception {
        this.mockMvc.perform(get("/recipe/1/delete")).andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/"));
        verify(this.recipeService, times(1)).deleteById(anyLong());
    }

    @Test
    public void GetRecipeNotFound() throws Exception {
        when(this.recipeService.getRecipe(anyLong())).thenThrow(NotFoundException.class);
        this.mockMvc.perform(get("/recipe?id=1")).andExpect(status().isNotFound()).andExpect(view().name("404error"));
    }
}