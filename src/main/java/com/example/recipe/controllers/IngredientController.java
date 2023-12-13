package com.example.recipe.controllers;

import com.example.recipe.commands.IngredientCommand;

import com.example.recipe.exceptions.NotFoundException;
import com.example.recipe.services.IngredientService;
import com.example.recipe.services.RecipeService;
import com.example.recipe.services.UnitOfMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    private WebDataBinder webDataBinder;

    @Autowired
    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        this.webDataBinder = webDataBinder;
    }

    @GetMapping("/recipe/{id}/ingredients")
    public String listIngredients(@PathVariable String id, Model model) throws NotFoundException {
        model.addAttribute("recipe", this.recipeService.getCommand(id));
        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String showIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        model.addAttribute("ingredient", this.ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
        return "recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        model.addAttribute("ingredient", this.ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
        model.addAttribute("uomList", this.unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/form";
    }

    @PostMapping
    @RequestMapping("/recipe/{id}/ingredient")
    public String saveOrUpdate(@PathVariable String id, String uomId, Model model, @ModelAttribute("ingredient") IngredientCommand ingredient) {

        this.webDataBinder.validate();

        BindingResult bindingResult = this.webDataBinder.getBindingResult();

        if (bindingResult.hasErrors()) {

            model.addAttribute("uomList", this.unitOfMeasureService.listAllUoms());

            return "recipe/ingredient/form";

        }

        this.unitOfMeasureService.findById(uomId).subscribe(ingredient::setUnitOfMeasure);
        this.ingredientService.saveIngredientCommand(ingredient);

        return ingredient.getId().equals("") ? "redirect:/recipe/" + id + "/ingredients" : "redirect:/recipe/" + id + "/ingredient/" + ingredient.getId() + "/show";

    }

    @GetMapping("/recipe/{id}/ingredient/new")
    public String newRecipeForm(@PathVariable String id, Model model) throws NotFoundException {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(id);
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", this.unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/form";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingredientId) {
        this.ingredientService.deleteIngredient(recipeId, ingredientId);
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
