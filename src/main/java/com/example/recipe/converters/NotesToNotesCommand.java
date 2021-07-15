package com.example.recipe.converters;

import com.example.recipe.commands.NotesCommand;
import com.example.recipe.domain.Notes;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NotesToNotesCommand implements Converter<Notes, NotesCommand> {
    @Override
    public NotesCommand convert(Notes notes) {
        return null;
    }
}
