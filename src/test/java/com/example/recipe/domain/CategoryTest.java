package com.example.recipe.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
    Category category;

    @BeforeEach
    public void setUp() {
        this.category = new Category();
    }

    @Test
    void getId() {
        String idValue = "4";
        this.category.setId(idValue);
        assertEquals(idValue, this.category.getId());
    }

    @Test
    void getDescription() {
    }

    @Test
    void getRecipes() {
    }
}