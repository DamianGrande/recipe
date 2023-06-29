package com.example.recipe.controllers;

import com.example.recipe.commands.IngredientCommand;
import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.converters.RecipeCommandToRecipe;
import com.example.recipe.services.IngredientService;
import com.example.recipe.services.RecipeService;
import com.example.recipe.services.UnitOfMeasureService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IngredientControllerTest {
    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    IngredientController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.controller = new IngredientController(this.recipeService, this.ingredientService, unitOfMeasureService, recipeCommandToRecipe);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
    }

    @Test
    void listIngredients() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        when(this.recipeService.getCommand(anyString())).thenReturn(recipeCommand);
        this.mockMvc.perform(get("/recipe/1/ingredients")).andExpect(status().isOk()).andExpect(view().name("recipe/ingredient/list")).andExpect(model().attributeExists("recipe"));
        verify(this.recipeService, times(1)).getCommand(anyString());
    }

    @Test
    void showIngredient() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        when(this.ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(ingredientCommand);
        this.mockMvc.perform(get("/recipe/1/ingredient/2/show")).andExpect(status().isOk()).andExpect(view().name("recipe/ingredient/show")).andExpect(model().attributeExists("ingredient"));
    }

    @Test
    public void updateIngredientForm() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        when(this.ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(ingredientCommand);
        when(this.unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());
        this.mockMvc.perform(get("/recipe/1/ingredient/2/update")).andExpect(view().name("recipe/ingredient/form")).andExpect(model().attributeExists("ingredient")).andExpect(model().attributeExists("uomList"));
    }

    @Test
    public void saveOrUpdate() throws Exception {
        IngredientCommand command = new IngredientCommand();
        command.setId("3");
        when(this.ingredientService.saveIngredientCommand(any())).thenReturn(command);
        this.mockMvc.perform(post("/recipe/2/ingredient").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("id", "").param("description", "some string")).andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/recipe/2/ingredient/3/show"));
    }

    @Test
    public void newIngredientForm() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("69");
        when(this.recipeService.getCommand(anyString())).thenReturn(recipeCommand);
        when(this.unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());
        this.mockMvc.perform(get("/recipe/69/ingredient/new")).andExpect(status().isOk()).andExpect(view().name("recipe/ingredient/form")).andExpect(model().attributeExists("ingredient")).andExpect(model().attributeExists("uomList")).andExpect(model().attribute("ingredient", hasProperty("recipeId", Matchers.equalTo("69"))));
        verify(this.recipeService, times(0)).getCommand(anyString());
    }

    @Test
    public void deleteIngredient() throws Exception {
        this.mockMvc.perform(get("/recipe/1/ingredient/3/delete")).andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/recipe/1/ingredients"));
        verify(this.ingredientService, times(1)).deleteIngredient("1", "3");
    }
}