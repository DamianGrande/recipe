package com.example.recipe.services;

import com.example.recipe.bootstrap.DataLoader;
import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.converters.RecipeCommandToRecipe;
import com.example.recipe.converters.RecipeToRecipeCommand;
import com.example.recipe.domain.Recipe;
import com.example.recipe.exceptions.NotFoundException;
import com.example.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.util.HashMap;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private Iterable<Recipe> recipes;
    private HashMap<String, String> images;

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
    public HashMap<String, String> getEncodedImages() throws IOException {
        this.populateRecipes();
        this.images = new HashMap<String, String>();
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
    public Recipe getRecipe(String id) throws NotFoundException {
        this.populateRecipes();
        for (Recipe recipe : this.recipes)
            if (recipe.getId().equals(id))
                return recipe;
        throw new NotFoundException("Recipe Not Found for id value: " + id);
    }

    private void populateRecipes() {
        this.recipes = this.recipeRepository.findAll();
    }

    @Override
    public RecipeCommand saveRecipeCommand(RecipeCommand command) throws NotFoundException {
        Recipe detachedRecipe = this.recipeCommandToRecipe.convert(command);
        if (this.getRecipe(command.getId()) != null)
            detachedRecipe.setImage(this.getRecipe(command.getId()).getImage());
        Recipe savedRecipe = this.recipeRepository.save(detachedRecipe);
        return this.recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    public RecipeCommand getCommand(String id) throws NotFoundException {
        return this.recipeToRecipeCommand.convert(this.getRecipe(id));
    }

    @Override
    public void deleteById(String id) {
        this.recipeRepository.deleteById(id);
    }
}
