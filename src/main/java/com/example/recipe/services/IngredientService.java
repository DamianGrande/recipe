package com.example.recipe.services;

import com.example.recipe.commands.IngredientCommand;
import reactor.core.publisher.Mono;

public interface IngredientService {

    Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

    void saveIngredientCommand(IngredientCommand ingredientCommand);

    void deleteIngredient(String recipeId, String ingredientId);

}
