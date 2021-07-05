package com.example.recipe.controllers;

import com.example.recipe.domain.Recipe;
import com.example.recipe.services.RecipeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;


class IndexControllerTest {
    private IndexController indexController;

    @Mock
    private RecipeServiceImpl recipeService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.indexController = new IndexController(this.recipeService);
    }

    @Test
    void getIndexPage() {
        Set<Recipe> recipes = new HashSet<Recipe>();
        recipes.add(new Recipe());
        recipes.add(new Recipe());
        when(this.recipeService.getRecipes()).thenReturn(recipes);
        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
        Assertions.assertEquals("index", this.indexController.getIndexPage(this.model));
        verify(this.recipeService, times(1)).getRecipes();
        verify(this.recipeService, times(1)).getEncodedImages();
        verify(this.model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        verify(this.model, times(1)).addAttribute("images", this.recipeService.getEncodedImages());
        Set<Recipe> setInController = argumentCaptor.getValue();
        Assertions.assertEquals(2, setInController.size());
    }
}