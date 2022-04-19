package ng.com.smartcity.recipeApp.converters;

import ng.com.smartcity.recipeApp.commands.NotesCommand;
import ng.com.smartcity.recipeApp.domain.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesCommandToNotesTest {

    private final static Long ID = 1L;
    private final static String RECIPENOTES = "recipe notes";
    NotesCommandToNotes converter;

    @BeforeEach
    void setUp() {
        converter = new NotesCommandToNotes();
    }

    @Test
    void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    void convert() {
        //given
        NotesCommand command = new NotesCommand();
        command.setId(ID);
        command.setRecipeNotes(RECIPENOTES);

        //then
        Notes notes = converter.convert(command);

        assertNotNull(notes);
        assertEquals(notes.getId(), ID);
    }
}