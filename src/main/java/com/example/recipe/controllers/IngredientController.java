package com.example.recipe.controllers;

import com.example.recipe.services.IngredientService;
import com.example.recipe.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    @RequestMapping("/recipe/{id}/ingredients")
    public String listIngredients(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", this.recipeService.getCommand(id));
        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String showIngredient(@PathVariable Long recipeId, @PathVariable Long ingredientId, Model model) {
        model.addAttribute("ingredient", this.ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
        return "recipe/ingredient/show";
    }
}
