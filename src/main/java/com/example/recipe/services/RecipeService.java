package com.example.recipe.services;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.domain.Recipe;
import com.example.recipe.exceptions.NotFoundException;

import java.io.IOException;
import java.util.HashMap;

public interface RecipeService {
    Iterable<Recipe> getRecipes();

    HashMap<Long, String> getEncodedImages() throws IOException;

    Recipe getRecipe(Long id) throws NotFoundException;

    RecipeCommand saveRecipeCommand(RecipeCommand command) throws NotFoundException;

    RecipeCommand getCommand(Long id) throws NotFoundException;

    void deleteById(Long id);
}
