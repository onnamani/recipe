package ng.com.smartcity.recipeApp.converters;

import ng.com.smartcity.recipeApp.commands.NotesCommand;
import ng.com.smartcity.recipeApp.domain.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesToNotesCommandTest {

    private final static Long ID = 1L;
    private final static String RECIPENOTES = "recipe notes";
    NotesToNotesCommand converter;

    @BeforeEach
    void setUp() {
        converter = new NotesToNotesCommand();
    }

    @Test
    void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    void convert() {
        //given
        Notes notes = new Notes();
        notes.setId(ID);
        notes.setRecipeNotes(RECIPENOTES);

        //then
        NotesCommand command = converter.convert(notes);

        assertNotNull(command);
        assertEquals(command.getId(), ID);
    }
}