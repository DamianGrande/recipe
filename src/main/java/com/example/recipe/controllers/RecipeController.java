package com.example.recipe.controllers;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.exceptions.NotFoundException;
import com.example.recipe.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404error");
        return modelAndView;
    }

    @GetMapping("/recipe")
    public String showRecipe(@RequestParam Long id, Model model) throws IOException, NotFoundException {
        model.addAttribute("recipe", this.recipeService.getRecipe(id));
        model.addAttribute("images", this.recipeService.getEncodedImages());
        return "recipe-detail";
    }

    @GetMapping("/recipe-form")
    public String showRecipeForm(Model model) {
        model.addAttribute("command", new RecipeCommand());
        return "form";
    }

    @PostMapping("/recipe/save")
    public String saveOrUpdate(@ModelAttribute RecipeCommand command) throws NotFoundException {
        RecipeCommand savedCommand = this.recipeService.saveRecipeCommand(command);
        return "redirect:/recipe?id=" + savedCommand.getId();
    }

    @GetMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable(name = "id") Long id, Model model) throws NotFoundException {
        model.addAttribute("command", this.recipeService.getCommand(id));
        return "form";
    }

    @GetMapping("recipe/{id}/delete")
    public String deleteRecipe(@PathVariable Long id) {
        this.recipeService.deleteById(id);
        return "redirect:/";
    }
}
