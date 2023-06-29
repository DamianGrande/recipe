package com.example.recipe.controllers;

import com.example.recipe.exceptions.NotFoundException;
import com.example.recipe.services.ImageService;
import com.example.recipe.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {
    private final ImageService imageService;
    private final RecipeService recipeService;

    @Autowired
    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}/image")
    public String getImageForm(@PathVariable String id, Model model) throws NotFoundException {
        model.addAttribute("recipe", this.recipeService.getCommand(id));
        return "image-upload-form";
    }

    @PostMapping("/recipe/{id}/image")
    public String handleImagePost(@PathVariable String id, @RequestParam("imageFile") MultipartFile file) throws Exception {
        this.imageService.saveImageFile(id, file);
        return "redirect:/recipe/?id=" + id;
    }
}
