package com.example.recipe.services;

import com.example.recipe.commands.IngredientCommand;
import com.example.recipe.converters.IngredientToIngredientCommand;
import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeRepository recipeRepository;

    @Autowired
    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, RecipeRepository recipeRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = this.recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty())
            log.error("Recipe id not found. Id: " + recipeId);
        Recipe recipe = recipeOptional.get();
        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream().filter(ingredient -> ingredient.getId().equals(ingredientId)).map(this.ingredientToIngredientCommand::convert).findFirst();
        if (ingredientCommandOptional.isEmpty())
            log.error("Ingredient id not found: " + ingredientId);
        return ingredientCommandOptional.get();
    }
}
