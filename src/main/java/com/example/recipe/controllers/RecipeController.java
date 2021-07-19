package com.example.recipe.controllers;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @RequestMapping("/recipe")
    public String showRecipe(@RequestParam Long id, Model model) throws IOException {
        model.addAttribute("recipe", this.recipeService.getRecipe(id));
        model.addAttribute("images", this.recipeService.getEncodedImages());
        return "recipe-detail";
    }

    @GetMapping
    @RequestMapping("/recipe-form")
    public String showRecipeForm(Model model) {
        model.addAttribute("command", new RecipeCommand());
        return "form";
    }

    @PostMapping
    @RequestMapping("/recipe/save")
    public String saveOrUpdate(@ModelAttribute RecipeCommand command) {
        RecipeCommand savedCommand = this.recipeService.saveRecipeCommand(command);
        return "redirect:/recipe?id=" + savedCommand.getId();
    }

    @GetMapping
    @RequestMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("command", this.recipeService.getCommand(id));
        return "form";
    }

    @GetMapping
    @RequestMapping("recipe/{id}/delete")
    public String deleteRecipe(@PathVariable Long id) {
        this.recipeService.deleteById(id);
        return "redirect:/";
    }
}
