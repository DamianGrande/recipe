package com.example.recipe.services;

import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private Iterable<Recipe> recipes;
    private HashMap<Long, String> images;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Iterable<Recipe> getRecipes() {
        log.debug("I'm in the service");
        this.populateRecipes();
        return this.recipes;
    }

    @Override
    public HashMap<Long, String> getEncodedImages() {
        if (this.images != null)
            return this.images;
        this.populateRecipes();
        this.images = new HashMap<Long, String>();
        for (Recipe recipe : this.recipes) {
            byte[] image = new byte[recipe.getImage().length];
            int index = 0;
            for (Byte byt : recipe.getImage())
                image[index++] = byt;
            this.images.put(recipe.getId(), Base64.encodeBase64String(image));
        }
        return this.images;
    }

    @Override
    public Recipe getRecipe(Long id) {
        this.populateRecipes();
        for (Recipe recipe : this.recipes)
            if (recipe.getId().equals(id))
                return recipe;
        return null;
    }

    private void populateRecipes() {
        if (this.recipes == null)
            this.recipes = this.recipeRepository.findAll();
    }
}
