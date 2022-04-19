package ng.com.smartcity.recipeApp.converters;

import lombok.Synchronized;
import ng.com.smartcity.recipeApp.commands.NotesCommand;
import ng.com.smartcity.recipeApp.domain.Notes;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {

    @Nullable
    @Synchronized
    @Override
    public Notes convert(NotesCommand source) {
        if(source == null)
            return null;

        Notes notes = new Notes();
        notes.setId(source.getId());
        notes.setRecipeNotes(source.getRecipeNotes());

        return notes;
    }
}
