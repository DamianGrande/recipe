package com.example.recipe.services.reactive;

import com.example.recipe.domain.Category;
import com.example.recipe.repositories.reactive.CategoryReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoryReactiveService {

    private final CategoryReactiveRepository repository;

    @Autowired
    public CategoryReactiveService(CategoryReactiveRepository repository) {
        this.repository = repository;
    }

    public Mono<Category> save(Category category) {
        return this.repository.save(category);
    }

    public Mono<Category> getByDescription(String description) {
        return this.repository.findCategoryByDescription(description);
    }

    public void deleteByDescription(String description) {
        if (this.getByDescription(description).block() != null)
            this.repository.deleteCategoryByDescription(description);
    }

    public Flux<Category> getAll() {
        return this.repository.findAll();
    }

}
