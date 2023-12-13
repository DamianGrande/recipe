package com.example.recipe.services;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.domain.Recipe;
import com.example.recipe.exceptions.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {

    Flux<Recipe> getRecipes();

    Mono<Recipe> getRecipe(String id) throws NotFoundException;

    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) throws NotFoundException;

    Mono<RecipeCommand> getCommand(String id) throws NotFoundException;

    void deleteById(String id);

}
