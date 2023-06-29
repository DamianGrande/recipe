package com.example.recipe.services;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.domain.Recipe;
import com.example.recipe.exceptions.NotFoundException;

import java.io.IOException;
import java.util.HashMap;

public interface RecipeService {
    Iterable<Recipe> getRecipes();

    HashMap<String, String> getEncodedImages() throws IOException;

    Recipe getRecipe(String id) throws NotFoundException;

    RecipeCommand saveRecipeCommand(RecipeCommand command) throws NotFoundException;

    RecipeCommand getCommand(String id) throws NotFoundException;

    void deleteById(String id);
}
