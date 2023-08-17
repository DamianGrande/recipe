package com.example.recipe.repositories.reactive;

import com.example.recipe.domain.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {

    Mono<Recipe> findRecipeByDescription(String description);

    void deleteRecipeByDescription(String description);

}
