package com.example.recipe.services;

import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private Iterable<Recipe> recipes;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Iterable<Recipe> getRecipes() {
        this.populateRecipes();
        return this.recipes;
    }

    @Override
    public HashMap<Long, String> getEncodedImages() {
        this.populateRecipes();
        HashMap<Long, String> map = new HashMap<Long, String>();
        for (Recipe recipe : this.recipes) {
            byte[] image = new byte[recipe.getImage().length];
            int index = 0;
            for (Byte byt : recipe.getImage())
                image[index++] = byt;
            map.put(recipe.getId(), Base64.encodeBase64String(image));
        }
        return map;
    }

    private void populateRecipes() {
        if (this.recipes == null)
            this.recipes = this.recipeRepository.findAll();
    }
}
