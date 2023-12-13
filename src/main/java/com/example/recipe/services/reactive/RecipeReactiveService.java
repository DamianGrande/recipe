package com.example.recipe.services.reactive;

import com.example.recipe.commands.RecipeCommand;
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

    @Autowired
    public RecipeReactiveService(RecipeReactiveRepository repository, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.repository = repository;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    public Mono<Recipe> save(Recipe recipe) {
        return this.repository.save(recipe);
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
        return this.repository.findRecipeById(id);
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) throws NotFoundException {
        return this.getRecipe(command.getId()).map(this.recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<RecipeCommand> getCommand(String id) throws NotFoundException {
        return this.getRecipe(id).map(this.recipeToRecipeCommand::convert);
    }

    @Override
    public void deleteById(String id) {
        this.repository.deleteById(id);
    }

}
