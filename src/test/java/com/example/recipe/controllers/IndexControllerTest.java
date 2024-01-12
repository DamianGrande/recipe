package com.example.recipe.controllers;

import com.example.recipe.domain.Recipe;
import com.example.recipe.services.reactive.RecipeReactiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@WebFluxTest(IndexController.class)
class IndexControllerTest {

    private IndexController indexController;

    @Autowired
    private ApplicationContext context;

    @MockBean
    private RecipeReactiveService recipeService;

    @MockBean
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.indexController = new IndexController(this.recipeService);
    }

    @Test
    public void testMockMVC() {

        WebTestClient webTestClient = WebTestClient.bindToApplicationContext(this.context).build();

        webTestClient.get().uri("/").exchange().expectStatus().isOk().expectBody();

    }

    @Test
    void getIndexPage() {

        when(this.recipeService.getRecipes()).thenReturn(Flux.just(new Recipe(), new Recipe()));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Flux<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Flux.class);

        assertEquals("index", this.indexController.getIndexPage(this.model));

        verify(this.recipeService, times(1)).getRecipes();
        verify(this.model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());

        Flux<Recipe> setInController = argumentCaptor.getValue();
        List<Recipe> recipes = setInController.collectList().block();

        assertNotNull(recipes);
        assertEquals(2, recipes.size());

    }

}