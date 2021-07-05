package com.example.recipe.controllers;

import com.example.recipe.services.RecipeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

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
        Assertions.assertEquals("index", this.indexController.getIndexPage(this.model));
        verify(this.recipeService, times(1)).getRecipes();
        verify(this.recipeService, times(1)).getEncodedImages();
        verify(this.model, times(1)).addAttribute("recipes", this.recipeService.getRecipes());
        verify(this.model, times(1)).addAttribute("images", this.recipeService.getEncodedImages());
    }
}