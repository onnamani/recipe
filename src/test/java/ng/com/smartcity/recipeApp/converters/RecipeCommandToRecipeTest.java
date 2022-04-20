package ng.com.smartcity.recipeApp.converters;

import ng.com.smartcity.recipeApp.commands.CategoryCommand;
import ng.com.smartcity.recipeApp.commands.IngredientCommand;
import ng.com.smartcity.recipeApp.commands.NotesCommand;
import ng.com.smartcity.recipeApp.commands.RecipeCommand;
import ng.com.smartcity.recipeApp.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecipeCommandToRecipeTest {

    private final static Long ID = 1L;
    private final static NotesCommand NOTES_COMMAND = new NotesCommand();
    private final static Set<IngredientCommand> INGREDIENTS = new HashSet<>();
    private final static Set<CategoryCommand> CATEGORIES = new HashSet<>();

    RecipeCommandToRecipe converter;

    @BeforeEach
    void setUp() {
        IngredientCommand ingredientCommand6 = new IngredientCommand();
        ingredientCommand6.setId(6L);
        INGREDIENTS.add(ingredientCommand6);

        NOTES_COMMAND.setId(8L);
        NOTES_COMMAND.setRecipeNotes("recipe notes");

        CategoryCommand categoryCommand9 = new CategoryCommand();
        categoryCommand9.setId(9L);
        CATEGORIES.add(categoryCommand9);

        UnitOfMeasureCommandToUnitOfMeasure uomConverter = new UnitOfMeasureCommandToUnitOfMeasure();
        IngredientCommandToIngredient ingredientConverter = new IngredientCommandToIngredient(uomConverter);
        NotesCommandToNotes notesConverter = new NotesCommandToNotes();
        CategoryCommandToCategory categoryConverter = new CategoryCommandToCategory();

        converter = new RecipeCommandToRecipe(ingredientConverter, notesConverter, categoryConverter);
    }

    @Test
    void testNullObject() {

        assertNull(converter.convert(null));

        RecipeCommand command = new RecipeCommand();

        assertNotNull(converter.convert(command));
    }

    @Test
    void convert() {
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId(ID);
        command.setNotes(NOTES_COMMAND);
        command.setIngredients(INGREDIENTS);
        command.setCategories(CATEGORIES);

        //then
        Recipe recipe = converter.convert(command);

        assertNotNull(recipe);
        assertEquals(recipe.getId(), ID);
        assertEquals(recipe.getCategories().size(), 1);
        assertInstanceOf(Category.class, recipe.getCategories().toArray()[0]);
        assertInstanceOf(Ingredient.class, recipe.getIngredients().toArray()[0]);
        assertInstanceOf(Notes.class, recipe.getNotes());
    }
}