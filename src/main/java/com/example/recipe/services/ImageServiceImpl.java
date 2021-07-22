package com.example.recipe.services;

import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) throws Exception {
        Recipe recipe = this.recipeRepository.findById(recipeId).get();
        Byte[] image = new Byte[file.getBytes().length];
        for (int i = 0; i < file.getBytes().length; i++)
            image[i] = file.getBytes()[i];
        recipe.setImage(image);
        this.recipeRepository.save(recipe);
    }
}
