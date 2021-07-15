package com.example.recipe.services;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.domain.Recipe;

import java.util.HashMap;

public interface RecipeService {
    Iterable<Recipe> getRecipes();

    HashMap<Long, String> getEncodedImages();

    Recipe getRecipe(Long id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);
}
