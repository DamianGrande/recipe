package com.example.recipe.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void saveImageFile(String recipeId, MultipartFile file) throws Exception;
}
