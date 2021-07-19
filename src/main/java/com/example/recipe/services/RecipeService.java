package com.example.recipe.services;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.domain.Recipe;

import java.io.IOException;
import java.util.HashMap;

public interface RecipeService {
    Iterable<Recipe> getRecipes();

    HashMap<Long, String> getEncodedImages() throws IOException;

    Recipe getRecipe(Long id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

    RecipeCommand getCommand(Long id);

    void deleteById(Long id);
}
