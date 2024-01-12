package com.example.recipe.services.reactive;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.converters.RecipeCommandToRecipe;
import com.example.recipe.converters.RecipeToRecipeCommand;
import com.example.recipe.domain.Recipe;
import com.example.recipe.exceptions.NotFoundException;
import com.example.recipe.repositories.reactive.RecipeReactiveRepository;
import com.example.recipe.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RecipeReactiveService implements RecipeService {

    private final RecipeReactiveRepository repository;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private final RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    public RecipeReactiveService(RecipeReactiveRepository repository, RecipeToRecipeCommand recipeToRecipeCommand, RecipeCommandToRecipe recipeCommandToRecipe) {
        this.repository = repository;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
    }

    public Mono<Recipe> save(Recipe recipe) {
        return this.repository.save(recipe).doOnError(throwable -> System.out.println("ERROR: " + throwable.getMessage()));
    }

    public Mono<Recipe> getByDescription(String description) {
        return this.repository.findRecipeByDescription(description);
    }

    public void deleteByDescription(String description) {
        if (this.getByDescription(description).block() != null)
            this.repository.deleteRecipeByDescription(description);
    }

    @Override
    public Flux<Recipe> getRecipes() {
        return this.repository.findAll();
    }

    @Override
    public Mono<Recipe> getRecipe(String id) throws NotFoundException {
        return this.repository.findRecipeById(id).switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) throws NotFoundException {
        return this.save(this.recipeCommandToRecipe.convert(command)).mapNotNull(this.recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<RecipeCommand> getCommand(String id) throws NotFoundException {
        return this.getRecipe(id).mapNotNull(this.recipeToRecipeCommand::convert);
    }

    @Override
    public void deleteById(String id) {
        this.repository.deleteById(id);
    }

}
