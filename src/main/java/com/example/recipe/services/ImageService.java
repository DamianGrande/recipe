package com.example.recipe.services;

import org.springframework.http.codec.multipart.FilePart;

public interface ImageService {
    void saveImageFile(String recipeId, FilePart file) throws Exception;
}
