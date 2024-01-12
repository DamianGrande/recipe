package com.example.recipe.services;

import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {

    @Mock
    RecipeReactiveRepository recipeRepository;

    @Mock
    FilePart filePart;

    @Mock
    DataBuffer dataBuffer;


    ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    void saveImageFile() throws Exception {

        String id = "1";
        int bufferLength = 3;
        Recipe recipe = new Recipe();
        recipe.setId(id);

        when(this.recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(this.filePart.content()).thenReturn(Flux.just(this.dataBuffer));
        when(this.dataBuffer.readableByteCount()).thenReturn(bufferLength);
        when(this.dataBuffer.asByteBuffer()).thenReturn(ByteBuffer.wrap(new byte[3]));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        this.imageService.saveImageFile(id, filePart);

        verify(this.recipeRepository, times(1)).save(argumentCaptor.capture());

        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(bufferLength, savedRecipe.getImage().length);

    }

}