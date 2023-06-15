package com.example.recipe.bootstrap;

import com.example.recipe.domain.Category;
import com.example.recipe.domain.UnitOfMeasure;
import com.example.recipe.repositories.CategoryRepository;
import com.example.recipe.repositories.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "prod"})
public class DataLoaderMySql implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    public DataLoaderMySql(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (this.categoryRepository.count() == 0)
            this.saveCategories();

        if (this.unitOfMeasureRepository.count() == 0)
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

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription("Teaspoon");
        this.unitOfMeasureRepository.save(unitOfMeasure);

        unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription("Tablespoon");
        this.unitOfMeasureRepository.save(unitOfMeasure);

        unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription("Cup");
        this.unitOfMeasureRepository.save(unitOfMeasure);

        unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription("Pinch");
        this.unitOfMeasureRepository.save(unitOfMeasure);

        unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription("Tablespoon");
        this.unitOfMeasureRepository.save(unitOfMeasure);

        unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription("Ounce");
        this.unitOfMeasureRepository.save(unitOfMeasure);

        unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription("Unit");
        this.unitOfMeasureRepository.save(unitOfMeasure);

    }

}
