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
import reactor.core.publisher.Mono;

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
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        return this.recipeReactiveRepository.findById(recipeId).map(recipe -> recipe.getIngredients().stream().filter(ingredient -> ingredient.getId().equals(ingredientId)).findFirst()).filter(Optional::isPresent).map(ingredient -> {
            IngredientCommand command = this.ingredientToIngredientCommand.convert(ingredient.get());
            command.setRecipeId(recipeId);
            return command;
        });


    }

    @Override
    public void saveIngredientCommand(IngredientCommand ingredientCommand) {

        this.recipeReactiveRepository.findById(ingredientCommand.getRecipeId()).subscribe(recipe -> {
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
            this.recipeReactiveRepository.save(recipe).block();
        });

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
