package com.example.recipe.controllers;

import com.example.recipe.commands.IngredientCommand;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.converters.RecipeCommandToRecipe;
import com.example.recipe.services.IngredientService;
import com.example.recipe.services.RecipeService;
import com.example.recipe.services.UnitOfMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;
    private final RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService, RecipeCommandToRecipe recipeCommandToRecipe) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
    }

    @GetMapping("/recipe/{id}/ingredients")
    public String listIngredients(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", this.recipeService.getCommand(id));
        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String showIngredient(@PathVariable Long recipeId, @PathVariable Long ingredientId, Model model) {
        model.addAttribute("ingredient", this.ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
        return "recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateIngredient(@PathVariable Long recipeId, @PathVariable Long ingredientId, Model model) {
        model.addAttribute("ingredient", this.ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
        model.addAttribute("uomList", this.unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/form";
    }

    @PostMapping
    @RequestMapping("/recipe/{id}/ingredient")
    public String saveOrUpdate(@PathVariable Long id, @ModelAttribute IngredientCommand ingredient, @Validated Long uomId) {
        ingredient.setUnitOfMeasure(this.unitOfMeasureService.findById(uomId));
        return "redirect:/recipe/" + id + "/ingredient/" + this.ingredientService.saveIngredientCommand(ingredient).getId() + "/show";
    }

    @GetMapping("/recipe/{id}/ingredient/new")
    public String newRecipeForm(@PathVariable Long id, Model model) {
        RecipeCommand recipeCommand = this.recipeService.getCommand(id);
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipe(this.recipeCommandToRecipe.convert(recipeCommand));
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", this.unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/form";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable Long recipeId, @PathVariable Long ingredientId) {
        this.ingredientService.deleteIngredient(recipeId, ingredientId);
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
