package com.example.recipe.controllers;

import com.example.recipe.services.ImageService;
import com.example.recipe.services.IngredientService;
import com.example.recipe.services.RecipeService;
import com.example.recipe.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebFluxTest(controllers = {ImageController.class, RecipeController.class, IngredientController.class})
public abstract class FluxControllerTest {

    WebTestClient webTestClient;

    ArgumentCaptor<Model> modelArgumentCaptor = ArgumentCaptor.forClass(Model.class);

    @Autowired
    private ApplicationContext context;

    @SpyBean
    @Autowired
    ImageController imageController;

    @SpyBean
    @Autowired
    RecipeController recipeController;

    @SpyBean
    @Autowired
    IngredientController ingredientController;

    @MockBean
    ImageService imageService;

    @MockBean
    RecipeService recipeService;

    @MockBean
    IngredientService ingredientService;

    @MockBean
    UnitOfMeasureService unitOfMeasureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    String getViewFromResponse(EntityExchangeResult<byte[]> response) {
        return response.toString().split("<title>")[1].split("</title>")[0].trim();
    }

    void checkModelAndView(Model model, String attribute, String view, EntityExchangeResult<byte[]> response) {

        assertNotNull(model);
        assertNotNull(model.getAttribute(attribute));
        assertEquals(view, this.getViewFromResponse(response));

    }

}
