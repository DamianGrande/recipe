package com.example.recipe.services.reactive;

import com.example.recipe.domain.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class CategoryReactiveServiceTest {

    @Autowired
    private CategoryReactiveService service;

    @AfterEach
    void tearDown() {
        this.service.deleteByDescription("Spicy");
    }

    @Test
    void save() {

        assertNull(this.service.getByDescription("Spicy").block());

        this.service.save(new Category("Spicy")).block();

        Category category = this.service.getByDescription("Spicy").block();

        this.checkCategory("Spicy", category);

    }

    @Test
    void getAll() {

        List<Category> categories = this.service.getAll().buffer().blockFirst();

        assertNotNull(categories);
        assertEquals(4, categories.size());

        this.checkCategory("American", categories.get(0));
        this.checkCategory("Italian", categories.get(1));
        this.checkCategory("Mexican", categories.get(2));
        this.checkCategory("FastFood", categories.get(3));

    }

    private void checkCategory(String description, Category category) {

        assertNotNull(category);
        assertEquals(description, category.getDescription());

    }

}