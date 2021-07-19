package com.example.recipe.services;

import com.example.recipe.bootstrap.DataLoader;
import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.converters.RecipeCommandToRecipe;
import com.example.recipe.converters.RecipeToRecipeCommand;
import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.codec.binary.Base64;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private Iterable<Recipe> recipes;
    private HashMap<Long, String> images;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Iterable<Recipe> getRecipes() {
        log.debug("I'm in the service");
        this.populateRecipes();
        return this.recipes;
    }

    @Override
    public HashMap<Long, String> getEncodedImages() throws IOException {
        this.populateRecipes();
        this.images = new HashMap<Long, String>();
        for (Recipe recipe : this.recipes) {
            if (recipe.getImage() == null)
                recipe.setImage(DataLoader.getBytesFromImage("static/images/recipes/default.jpeg"));
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
        this.recipes = this.recipeRepository.findAll();
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = this.recipeCommandToRecipe.convert(command);
        if (this.getRecipe(command.getId()) != null)
            detachedRecipe.setImage(this.getRecipe(command.getId()).getImage());
        Recipe savedRecipe = this.recipeRepository.save(detachedRecipe);
        return this.recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    @Transactional
    public RecipeCommand getCommand(Long id) {
        return this.recipeToRecipeCommand.convert(this.getRecipe(id));
    }

    @Override
    public void deleteById(Long id) {
        this.recipeRepository.deleteById(id);
    }
}
