package com.example.recipe.services;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.converters.RecipeCommandToRecipe;
import com.example.recipe.converters.RecipeToRecipeCommand;
import com.example.recipe.domain.Recipe;
import com.example.recipe.exceptions.NotFoundException;
import com.example.recipe.repositories.RecipeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;


import java.util.Objects;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipeServiceIT {
    public static final String NEW_DESCRIPTION = "New Description.";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Test
    public void testSaveOfDescription() throws NotFoundException {

        Iterable<Recipe> recipes = this.recipeRepository.findAll();
        Recipe testRecipe = recipes.iterator().next();
        String oldDescription = testRecipe.getDescription();

        RecipeCommand testRecipeCommand = this.recipeToRecipeCommand.convert(testRecipe);
        Objects.requireNonNull(testRecipeCommand).setDescription(RecipeServiceIT.NEW_DESCRIPTION);
        Mono<RecipeCommand> savedRecipeCommand = this.recipeService.saveRecipeCommand(testRecipeCommand);

        assertEquals(RecipeServiceIT.NEW_DESCRIPTION, Objects.requireNonNull(savedRecipeCommand.block()).getDescription());
        assertEquals(testRecipe.getId(), Objects.requireNonNull(savedRecipeCommand.block()).getId());
        assertEquals(testRecipe.getCategories().size(), Objects.requireNonNull(savedRecipeCommand.block()).getCategories().size());
        assertEquals(testRecipe.getIngredients().size(), Objects.requireNonNull(savedRecipeCommand.block()).getIngredients().size());

        testRecipeCommand.setDescription(oldDescription);
        savedRecipeCommand = this.recipeService.saveRecipeCommand(testRecipeCommand);

        assertEquals(oldDescription, Objects.requireNonNull(savedRecipeCommand.block()).getDescription());

    }

}
