package ng.com.smartcity.recipeApp.converters;

import lombok.Synchronized;
import ng.com.smartcity.recipeApp.commands.NotesCommand;
import ng.com.smartcity.recipeApp.domain.Notes;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NotesToNotesCommand implements Converter<Notes, NotesCommand> {

    @Synchronized
    @Override
    public NotesCommand convert(Notes source) {
        if(source == null)
            return null;

        NotesCommand command = new NotesCommand();
        command.setId(source.getId());
        command.setRecipeNotes(source.getRecipeNotes());

        return command;
    }
}
