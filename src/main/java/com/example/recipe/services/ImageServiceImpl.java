package com.example.recipe.services;

import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.reactive.RecipeReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeReactiveRepository recipeRepository;

    @Autowired
    public ImageServiceImpl(RecipeReactiveRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(String recipeId, MultipartFile file) throws Exception {
        Recipe recipe = this.recipeRepository.findById(recipeId).block();
        Byte[] image = new Byte[file.getBytes().length];
        for (int i = 0; i < file.getBytes().length; i++)
            image[i] = file.getBytes()[i];
        recipe.setImage(image);
        this.recipeRepository.save(recipe);
    }
}
