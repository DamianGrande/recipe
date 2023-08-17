package com.example.recipe.bootstrap;

import com.example.recipe.domain.Category;
import com.example.recipe.domain.UnitOfMeasure;
import com.example.recipe.repositories.CategoryRepository;
import com.example.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "prod"})
public class DataLoaderMySql implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    @Autowired
    public DataLoaderMySql(CategoryRepository categoryRepository, UnitOfMeasureReactiveRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void run(String... args) {

        if (this.categoryRepository.count() == 0)
            this.saveCategories();

        if (this.unitOfMeasureRepository.count().block() == 0)
            this.saveUnitsOfMeasure();

    }

    private void saveCategories() {

        Category category = new Category();
        category.setDescription("American");
        this.categoryRepository.save(category);

        category = new Category();
        category.setDescription("Italian");
        this.categoryRepository.save(category);

        category = new Category();
        category.setDescription("Mexican");
        this.categoryRepository.save(category);

        category = new Category();
        category.setDescription("FastFood");
        this.categoryRepository.save(category);

    }

    private void saveUnitsOfMeasure() {

        this.unitOfMeasureRepository.save(new UnitOfMeasure("Teaspoon"));
        this.unitOfMeasureRepository.save(new UnitOfMeasure("Tablespoon"));
        this.unitOfMeasureRepository.save(new UnitOfMeasure("Cup"));
        this.unitOfMeasureRepository.save(new UnitOfMeasure("Pinch"));
        this.unitOfMeasureRepository.save(new UnitOfMeasure("Tablespoon"));
        this.unitOfMeasureRepository.save(new UnitOfMeasure("Ounce"));
        this.unitOfMeasureRepository.save(new UnitOfMeasure("Unit"));

    }

}
