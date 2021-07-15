package com.example.recipe.converters;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.domain.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {
    private final CategoryToCategoryCommand categoryConverter;
    private final IngredientToIngredientCommand ingredientConverter;
    private final NotesToNotesCommand notesConverter;

    public RecipeToRecipeCommand(CategoryToCategoryCommand categoryConverter, IngredientToIngredientCommand ingredientConverter, NotesToNotesCommand notesConverter) {
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Override
    public RecipeCommand convert(Recipe recipe) {
        if (recipe == null)
            return null;
        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(recipe.getId());
        recipeCommand.setCookTime(recipe.getCookTime());
        recipeCommand.setPrepTime(recipe.getPrepTime());
        recipeCommand.setDescription(recipe.getDescription());
        recipeCommand.setDifficulty(recipe.getDifficulty());
        recipeCommand.setDirections(recipe.getDirections());
        recipeCommand.setServings(recipe.getServings());
        recipeCommand.setSource(recipe.getSource());
        recipeCommand.setUrl(recipe.getUrl());
        recipeCommand.setNotes(this.notesConverter.convert(recipe.getNotes()));
        if (recipe.getCategories() != null && recipe.getCategories().size() > 0)
            recipe.getCategories().forEach(category -> recipeCommand.getCategories().add(this.categoryConverter.convert(category)));
        if (recipe.getIngredients() != null && recipe.getIngredients().size() > 0)
            recipe.getIngredients().forEach(ingredient -> recipeCommand.getIngredients().add(this.ingredientConverter.convert(ingredient)));
        return recipeCommand;
    }
}
