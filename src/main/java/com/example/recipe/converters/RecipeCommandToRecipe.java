package com.example.recipe.converters;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.domain.Category;
import com.example.recipe.domain.Ingredient;
import com.example.recipe.domain.Recipe;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {
    private final CategoryCommandToCategory categoryConverter;
    private final IngredientCommandToIngredient ingredientConverter;
    private final NotesCommandToNotes notesConverter;

    @Autowired
    public RecipeCommandToRecipe(CategoryCommandToCategory categoryConverter, IngredientCommandToIngredient ingredientConverter, NotesCommandToNotes notesConverter) {
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Override
    @Synchronized
    @Nullable
    public Recipe convert(RecipeCommand recipeCommand) {
        if (recipeCommand == null)
            return null;
        final Recipe recipe = new Recipe();
        recipe.setId(recipeCommand.getId());
        recipe.setCookTime(recipeCommand.getCookTime());
        recipe.setPrepTime(recipeCommand.getPrepTime());
        recipe.setDescription(recipeCommand.getDescription());
        recipe.setDifficulty(recipeCommand.getDifficulty());
        recipe.setDirections(recipeCommand.getDirections());
        recipe.setServings(recipeCommand.getServings());
        recipe.setSource(recipeCommand.getSource());
        recipe.setUrl(recipeCommand.getUrl());
        recipe.setNotes(this.notesConverter.convert(recipeCommand.getNotes()));
        if (recipeCommand.getCategories() != null && recipeCommand.getCategories().size() > 0) {
            recipe.setCategories(new HashSet<Category>());
            recipeCommand.getCategories().forEach(categoryCommand -> recipe.getCategories().add(this.categoryConverter.convert(categoryCommand)));
        }
        if (recipeCommand.getIngredients() != null && recipeCommand.getIngredients().size() > 0) {
            recipe.setIngredients(new HashSet<Ingredient>());
            recipeCommand.getIngredients().forEach(ingredientCommand -> recipe.getIngredients().add(this.ingredientConverter.convert(ingredientCommand)));
        }
        return recipe;
    }
}
