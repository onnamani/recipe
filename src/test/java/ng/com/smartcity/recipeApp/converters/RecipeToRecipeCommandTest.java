package ng.com.smartcity.recipeApp.converters;

import ng.com.smartcity.recipeApp.commands.CategoryCommand;
import ng.com.smartcity.recipeApp.commands.IngredientCommand;
import ng.com.smartcity.recipeApp.commands.NotesCommand;
import ng.com.smartcity.recipeApp.commands.RecipeCommand;
import ng.com.smartcity.recipeApp.domain.Category;
import ng.com.smartcity.recipeApp.domain.Ingredient;
import ng.com.smartcity.recipeApp.domain.Notes;
import ng.com.smartcity.recipeApp.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecipeToRecipeCommandTest {

    private final static Long ID = 1L;
    private final static Notes NOTES = new Notes();
    private final static Set<Category> CATEGORIES = new HashSet<>();
    private final static Set<Ingredient> INGREDIENTS = new HashSet<>();

    RecipeToRecipeCommand recipeConverter;

    @BeforeEach
    void setUp() {
        NOTES.setId(2L);

        Category category = new Category();
        category.setId(3L);
        CATEGORIES.add(category);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(4L);
        INGREDIENTS.add(ingredient);

        UnitOfMeasureToUnitOfMeasureCommand uomConverter = new UnitOfMeasureToUnitOfMeasureCommand();
        IngredientToIngredientCommand ingredientConverter = new IngredientToIngredientCommand(uomConverter);
        NotesToNotesCommand notesConverter = new NotesToNotesCommand();
        CategoryToCategoryCommand categoryConverter = new CategoryToCategoryCommand();

        recipeConverter = new RecipeToRecipeCommand(ingredientConverter, notesConverter, categoryConverter);

    }

    @Test
    void testNullObject() {
        assertNull(recipeConverter.convert(null));

        Recipe recipe = new Recipe();

        assertNotNull(recipeConverter.convert(recipe));
    }

    @Test
    void convert() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(ID);
        recipe.setNotes(NOTES);
        recipe.setCategories(CATEGORIES);
        recipe.setIngredients(INGREDIENTS);

        //then
        RecipeCommand command = recipeConverter.convert(recipe);

        assertNotNull(command);
        assertEquals(command.getId(), ID);
        assertEquals(command.getCategories().size(), 1);
        assertEquals(command.getIngredients().size(), 1);
        assertInstanceOf(CategoryCommand.class, command.getCategories().toArray()[0]);
        assertInstanceOf(IngredientCommand.class, command.getIngredients().toArray()[0]);
        assertInstanceOf(NotesCommand.class, command.getNotes());
    }
}