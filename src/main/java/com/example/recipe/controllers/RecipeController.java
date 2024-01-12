package com.example.recipe.controllers;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.exceptions.NotFoundException;
import com.example.recipe.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.exceptions.TemplateInputException;

@Controller
public class RecipeController {

    private final RecipeService recipeService;

    private WebDataBinder webDataBinder;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class, TemplateInputException.class})
    public String handleNotFound(Exception exception, Model model) {
        model.addAttribute("exception", exception);
        return "404error";
    }


    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        this.webDataBinder = webDataBinder;
    }

    @GetMapping("/recipe")
    public String showRecipe(@RequestParam String id, Model model) throws NotFoundException {

        model.addAttribute("recipe", this.recipeService.getRecipe(id));

        return "recipe-detail";

    }

    @GetMapping("/recipe-form")
    public String showRecipeForm(Model model) {
        model.addAttribute("command", new RecipeCommand());
        return "form";
    }

    @PostMapping("/recipe/save")
    public String saveOrUpdate(@ModelAttribute("command") RecipeCommand command) throws NotFoundException {
        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();
        if (bindingResult.hasErrors())
            return "form";
        this.recipeService.saveRecipeCommand(command).subscribe();
        return "redirect:/recipe?id=" + command.getId();
    }

    @GetMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable(name = "id") String id, Model model) throws NotFoundException {
        model.addAttribute("command", this.recipeService.getCommand(id));
        return "form";
    }

    @GetMapping("recipe/{id}/delete")
    public String deleteRecipe(@PathVariable String id) {
        this.recipeService.deleteById(id);
        return "redirect:/";
    }
}
