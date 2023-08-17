package com.example.recipe.services.reactive;

import com.example.recipe.domain.UnitOfMeasure;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class UnitOfMeasureReactiveServiceTest {

    @Autowired
    private UnitOfMeasureReactiveService service;

    @After
    public void tearDown() {
        this.service.deleteByDescription("Kilogram");
    }

    @Test
    void save() {

        assertNull(this.service.getByDescription("Kilogram").block());

        this.service.save(new UnitOfMeasure("Kilogram")).block();

        UnitOfMeasure unitOfMeasure = this.service.getByDescription("Kilogram").block();

        this.checkUnitOfMeasure("Kilogram", unitOfMeasure);

    }

    @Test
    void getAll() {

        List<UnitOfMeasure> unitsOfMeasure = this.service.getAll().buffer().blockFirst();

        assertNotNull(unitsOfMeasure);
        assertEquals(6, unitsOfMeasure.size());

        this.checkUnitOfMeasure("Teaspoon", unitsOfMeasure.get(0));
        this.checkUnitOfMeasure("Tablespoon", unitsOfMeasure.get(1));
        this.checkUnitOfMeasure("Cup", unitsOfMeasure.get(2));
        this.checkUnitOfMeasure("Pinch", unitsOfMeasure.get(3));
        this.checkUnitOfMeasure("Ounce", unitsOfMeasure.get(4));
        this.checkUnitOfMeasure("Unit", unitsOfMeasure.get(5));

    }


    private void checkUnitOfMeasure(String description, UnitOfMeasure unitOfMeasure) {

        assertNotNull(unitOfMeasure);
        assertEquals(description, unitOfMeasure.getDescription());

    }

}