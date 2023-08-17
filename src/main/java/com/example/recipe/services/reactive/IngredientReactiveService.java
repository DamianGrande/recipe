package com.example.recipe.services.reactive;

import com.example.recipe.commands.IngredientCommand;
import com.example.recipe.converters.IngredientCommandToIngredient;
import com.example.recipe.converters.IngredientToIngredientCommand;
import com.example.recipe.domain.Ingredient;
import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.reactive.IngredientReactiveRepository;
import com.example.recipe.repositories.reactive.RecipeReactiveRepository;
import com.example.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import com.example.recipe.services.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientReactiveService implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    private final IngredientReactiveRepository ingredientReactiveRepository;

    @Autowired
    public IngredientReactiveService(IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient, RecipeReactiveRepository recipeReactiveRepository, UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository, IngredientReactiveRepository ingredientReactiveRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.ingredientReactiveRepository = ingredientReactiveRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        Optional<Recipe> recipeOptional = this.recipeReactiveRepository.findById(recipeId).blockOptional();
        if (recipeOptional.isEmpty())
            log.error("Recipe id not found. Id: " + recipeId);
        Recipe recipe = recipeOptional.get();
        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream().filter(ingredient -> ingredient.getId().equals(ingredientId)).map(this.ingredientToIngredientCommand::convert).findFirst();
        if (ingredientCommandOptional.isEmpty())
            log.error("Ingredient id not found: " + ingredientId);
        IngredientCommand ingredientCommand = ingredientCommandOptional.get();
        ingredientCommand.setRecipeId(recipeId);
        return ingredientCommand;
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Optional<Recipe> recipeOptional = this.recipeReactiveRepository.findById(ingredientCommand.getRecipeId()).blockOptional();
        if (recipeOptional.isEmpty()) {
            log.error("Recipe not found for id: " + ingredientCommand.getRecipeId());
            return new IngredientCommand();
        }
        Recipe recipe = recipeOptional.get();
        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream().filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId())).findFirst();
        if (ingredientOptional.isPresent()) {
            Ingredient ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(ingredientCommand.getDescription());
            ingredientFound.setAmount(ingredientCommand.getAmount());
            ingredientFound.setUnitOfMeasure(this.unitOfMeasureReactiveRepository.findById(ingredientCommand.getUnitOfMeasure().getId()).blockOptional().orElseThrow(() -> new RuntimeException("UOM NOT FOUND.")));
        } else {
            Ingredient newIngredient = this.ingredientCommandToIngredient.convert(ingredientCommand);
            newIngredient.setId(null);
            recipe.addIngredient(newIngredient);
            newIngredient = this.ingredientReactiveRepository.save(newIngredient).block();
            ingredientCommand.setId(newIngredient.getId());
        }
        Recipe savedRecipe = this.recipeReactiveRepository.save(recipe).block();
        return this.ingredientToIngredientCommand.convert(savedRecipe.getIngredients().stream().filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId())).findFirst().get());
    }

    @Override
    public void deleteIngredient(String recipeId, String ingredientId) {
        Recipe recipe = this.recipeReactiveRepository.findById(recipeId).blockOptional().orElseThrow();
        Ingredient ingredientToDelete = recipe.getIngredients().stream().filter(ingredient -> ingredient.getId().equals(ingredientId)).findFirst().orElseThrow();
        this.ingredientReactiveRepository.deleteById(ingredientToDelete.getId()).block();
        recipe.getIngredients().removeIf(ingredient -> ingredient.getId().equals(ingredientToDelete.getId()));
        this.recipeReactiveRepository.save(recipe).block();
    }

}
