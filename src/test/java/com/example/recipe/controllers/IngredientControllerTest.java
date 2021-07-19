package com.example.recipe.controllers;

import com.example.recipe.commands.IngredientCommand;
import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.services.IngredientService;
import com.example.recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IngredientControllerTest {
    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    IngredientController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.controller = new IngredientController(this.recipeService, this.ingredientService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
    }

    @Test
    void listIngredients() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        when(this.recipeService.getCommand(anyLong())).thenReturn(recipeCommand);
        this.mockMvc.perform(get("/recipe/1/ingredients")).andExpect(status().isOk()).andExpect(view().name("recipe/ingredient/list")).andExpect(model().attributeExists("recipe"));
        verify(this.recipeService, times(1)).getCommand(anyLong());
    }

    @Test
    void showIngredient() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        when(this.ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);
        this.mockMvc.perform(get("/recipe/1/ingredient/2/show")).andExpect(status().isOk()).andExpect(view().name("recipe/ingredient/show")).andExpect(model().attributeExists("ingredient"));
    }
}