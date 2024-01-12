package com.example.recipe.controllers;

import com.example.recipe.commands.RecipeCommand;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.ui.Model;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

class ImageControllerTest extends FluxControllerTest {

    @Test
    void getImageForm() {

        RecipeCommand command = new RecipeCommand();
        command.setId("1");

        when(this.recipeService.getCommand(anyString())).thenReturn(Mono.just(command));

        EntityExchangeResult<byte[]> response = this.webTestClient.get().uri("/recipe/1/image").exchange().expectStatus().isOk().expectBody().returnResult();

        verify(this.recipeService, times(1)).getCommand(anyString());
        verify(super.imageController, times(1)).getImageForm(eq("1"), modelArgumentCaptor.capture());

        Model model = modelArgumentCaptor.getValue();

        super.checkModelAndView(model, "recipe", "Image Upload Form", response);

    }

    @Test
    void handleImagePost() throws Exception {

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("image", new ClassPathResource("static/images/recipes/default.jpeg")).headers(h -> {
            h.setContentDispositionFormData("imageFile", "test-image.png");
            h.setContentType(MediaType.IMAGE_JPEG);
        });

        this.webTestClient.post().uri("/recipe/1/image").contentType(MediaType.MULTIPART_FORM_DATA).bodyValue(multipartBodyBuilder.build()).exchange().expectStatus().is3xxRedirection().expectHeader().valueEquals(HttpHeaders.LOCATION, "/recipe/?id=1");

        verify(this.imageService, times(1)).saveImageFile(anyString(), any());

    }

}
