package com.example.recipe.converters;

import com.example.recipe.commands.NotesCommand;
import com.example.recipe.domain.Notes;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {
    @Override
    public Notes convert(NotesCommand notesCommand) {
        return new Notes(notesCommand.getId(), notesCommand.getRecipeNotes());
    }
}
