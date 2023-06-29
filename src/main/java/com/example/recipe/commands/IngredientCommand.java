package com.example.recipe.commands;

import com.example.recipe.domain.UnitOfMeasure;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand {
    private String recipeId;
    private String id;
    private String description;
    private BigDecimal amount;

    @Getter(AccessLevel.NONE)
    private UnitOfMeasure unitOfMeasure;

    public UnitOfMeasure getUnitOfMeasure() {
        if (this.unitOfMeasure == null)
            return new UnitOfMeasure();
        return this.unitOfMeasure;
    }

}
