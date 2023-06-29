package com.example.recipe.converters;

import com.example.recipe.commands.CategoryCommand;
import com.example.recipe.domain.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand> {
    @Override
    public CategoryCommand convert(Category category) {
        return new CategoryCommand(category.getId(), category.getDescription());
    }
}
