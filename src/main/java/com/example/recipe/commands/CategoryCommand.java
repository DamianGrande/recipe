package com.example.recipe.commands;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCommand {
    private String id;
    private String description;

    public CategoryCommand() {
    }

    public CategoryCommand(String id, String description) {
        this.id = id;
        this.description = description;
    }

}
