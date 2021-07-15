package com.example.recipe.converters;

import com.example.recipe.commands.IngredientCommand;
import com.example.recipe.domain.Ingredient;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {
    @Override
    public Ingredient convert(IngredientCommand ingredientCommand) {
        return null;
    }
}
