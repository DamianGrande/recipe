package com.example.recipe.converters;

import com.example.recipe.commands.UnitOfMeasureCommand;
import com.example.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureCommandToUnitOfMeasureTest {
    public static final String DESCRIPTION = "description";
    public static final Long LONG_VALUE = 1L;
    UnitOfMeasureCommandToUnitOfMeasure converter;

    @BeforeEach
    void setUp() {
        this.converter = new UnitOfMeasureCommandToUnitOfMeasure();
    }

    @Test
    public void testNullParameter() {
        assertNull(this.converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        assertNotNull(this.converter.convert(new UnitOfMeasureCommand()));
    }

    @Test
    void convert() {
        UnitOfMeasureCommand command = new UnitOfMeasureCommand();
        command.setId(UnitOfMeasureCommandToUnitOfMeasureTest.LONG_VALUE);
        command.setDescription(UnitOfMeasureCommandToUnitOfMeasureTest.DESCRIPTION);
        UnitOfMeasure unitOfMeasure = this.converter.convert(command);
        assertNotNull(unitOfMeasure);
        assertEquals(UnitOfMeasureCommandToUnitOfMeasureTest.LONG_VALUE, unitOfMeasure.getId());
        assertEquals(UnitOfMeasureCommandToUnitOfMeasureTest.DESCRIPTION, unitOfMeasure.getDescription());
    }
}