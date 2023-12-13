package com.example.recipe.domain;

import com.example.recipe.bootstrap.DataLoader;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Document
public class Recipe {

    @Id
    private String id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Byte[] image;
    private Difficulty difficulty;
    private Notes notes;

    public Recipe() {
    }

    public Recipe(String id) {
        this.id = id;
    }

    @Getter(AccessLevel.NONE)
    private Set<Ingredient> ingredients;

    @DBRef
    private Set<Category> categories;

    public void addIngredient(Ingredient ingredient) {
        if (this.ingredients == null)
            this.ingredients = new HashSet<Ingredient>();
        this.ingredients.add(ingredient);
    }

    public Set<Ingredient> getIngredients() {
        if (this.ingredients == null)
            return new HashSet<Ingredient>();
        return this.ingredients;
    }

    public String getImageAsBase64String() throws IOException {

        if (this.getImage() == null)
            this.setImage(DataLoader.getBytesFromImage("static/images/recipes/default.jpeg"));


        byte[] image = new byte[this.getImage().length];
        int index = 0;
        for (Byte byt : this.getImage())
            image[index++] = byt;

        return Base64.encodeBase64String(image);

    }

}
