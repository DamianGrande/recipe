package com.example.recipe.services;

import com.example.recipe.repositories.reactive.RecipeReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeReactiveRepository recipeRepository;

    @Autowired
    public ImageServiceImpl(RecipeReactiveRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(String recipeId, FilePart file) {
        this.recipeRepository.findById(recipeId).subscribe(recipe -> file.content().subscribe(dataBuffer -> {
            int dataBufferLength = dataBuffer.readableByteCount();
            byte[] bytes = dataBuffer.asByteBuffer().array();
            Byte[] image = new Byte[dataBufferLength];
            for (int i = 0; i < dataBufferLength; i++)
                image[i] = bytes[i];
            recipe.setImage(image);
            this.recipeRepository.save(recipe).subscribe();
        }));
    }

}
