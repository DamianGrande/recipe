package com.example.recipe.controllers;

import com.example.recipe.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping("/recipe")
    public String showRecipe(@RequestParam Long id, Model model) {
        model.addAttribute("recipe", this.recipeService.getRecipe(id));
        model.addAttribute("images", this.recipeService.getEncodedImages());
        return "recipe";
    }
}
