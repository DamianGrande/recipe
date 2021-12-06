package com.example.recipe.controllers;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.services.ImageService;
import com.example.recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ImageControllerTest {
    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    ImageController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.controller = new ImageController(this.imageService, this.recipeService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).setControllerAdvice(new ControllerExceptionHandler()).build();
    }

    @Test
    void getImageForm() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);
        when(this.recipeService.getCommand(anyLong())).thenReturn(command);
        this.mockMvc.perform(get("/recipe/1/image")).andExpect(status().isOk()).andExpect(model().attributeExists("recipe")).andExpect(view().name("image-upload-form"));
        verify(this.recipeService, times(1)).getCommand(anyLong());
    }

    @Test
    void handleImagePost() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("imageFile", "testing.txt", "text/plain", "Spring Framework Guru".getBytes());
        this.mockMvc.perform(multipart("/recipe/1/image").file(multipartFile)).andExpect(status().is3xxRedirection()).andExpect(header().string("Location", "/recipe/?id=1"));
        verify(this.imageService, times(1)).saveImageFile(anyLong(), any());
    }

    @Test
    public void testGetImageNumberFormatException() throws Exception {
        this.mockMvc.perform(get("/recipe/asdf/image")).andExpect(status().isBadRequest()).andExpect(view().name("400error"));
    }
}