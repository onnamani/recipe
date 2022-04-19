package ng.com.smartcity.recipeApp.converters;

import ng.com.smartcity.recipeApp.commands.CategoryCommand;
import ng.com.smartcity.recipeApp.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryCommandToCategoryTest {

    private final static Long ID = 1L;
    private final static String DESCRIPTION = "description";
    CategoryCommandToCategory converter;

    @BeforeEach
    void setUp() {
        converter = new CategoryCommandToCategory();
    }

    @Test
    void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    void convert() {
        //given
        CategoryCommand command = new CategoryCommand();
        command.setId(ID);
        command.setDescription(DESCRIPTION);

        //then
        Category category = converter.convert(command);

        assertNotNull(category);
        assertEquals(category.getId(), ID);
    }
}