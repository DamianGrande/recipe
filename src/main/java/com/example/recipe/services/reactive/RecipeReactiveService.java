package com.example.recipe.services.reactive;

import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.reactive.RecipeReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RecipeReactiveService {

    private final RecipeReactiveRepository repository;

    @Autowired
    public RecipeReactiveService(RecipeReactiveRepository repository) {
        this.repository = repository;
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

    public Flux<Recipe> getAll() {
        return this.repository.findAll();
    }

}
