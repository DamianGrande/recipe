package com.example.recipe.controllers;

import com.example.recipe.domain.Recipe;
import com.example.recipe.services.reactive.RecipeReactiveService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;

import java.util.Set;

import static org.mockito.Mockito.*;


class IndexControllerTest {
    private IndexController indexController;

    @Mock
    private RecipeReactiveService recipeService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.indexController = new IndexController(this.recipeService);
    }

    @Test
    public void testMockMVC() {

        WebTestClient webTestClient = WebTestClient.bindToController(this.indexController).build();

        webTestClient.get().uri("/").exchange().expectStatus().isOk().expectBody();

    }

    @Test
    void getIndexPage() {

        when(this.recipeService.getRecipes()).thenReturn(Flux.just(new Recipe(), new Recipe()));

        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        Assertions.assertEquals("index", this.indexController.getIndexPage(this.model));

        verify(this.recipeService, times(1)).getRecipes();
        verify(this.model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());

        Set<Recipe> setInController = argumentCaptor.getValue();

        Assertions.assertEquals(2, setInController.size());

    }
}