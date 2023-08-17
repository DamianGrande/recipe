package com.example.recipe.repositories.reactive;

import com.example.recipe.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CategoryReactiveRepository extends ReactiveMongoRepository<Category, String> {

    Mono<Category> findCategoryByDescription(String description);

    void deleteCategoryByDescription(String description);

}
