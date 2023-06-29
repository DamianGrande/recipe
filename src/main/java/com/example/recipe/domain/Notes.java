package com.example.recipe.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Notes {

    @Id
    private String id;
    private String recipeNotes;

    public Notes() {
    }

    public Notes(String id) {
        this.id = id;
    }

    public Notes(String id, String recipeNotes) {
        this.id = id;
        this.recipeNotes = recipeNotes;
    }

}
