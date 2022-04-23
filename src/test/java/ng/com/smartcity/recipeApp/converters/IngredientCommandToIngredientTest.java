package ng.com.smartcity.recipeApp.converters;

import ng.com.smartcity.recipeApp.commands.IngredientCommand;
import ng.com.smartcity.recipeApp.commands.UnitOfMeasureCommand;
import ng.com.smartcity.recipeApp.domain.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientCommandToIngredientTest {

    private final static Long ID = 1L;
    private final static String DESCRIPTION = "description";
    private final static BigDecimal AMOUNT = new BigDecimal(1);
    private static UnitOfMeasureCommand uomCommand;

    IngredientCommandToIngredient ingredientConverter;

    @BeforeEach
    void setUp() {
        uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(2L);
        uomCommand.setDescription(DESCRIPTION);
        ingredientConverter = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Test
    void testNullObject() {
        assertNull(ingredientConverter.convert(null));
    }

    @Test
    void convert() {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId(ID);
        command.setAmount(AMOUNT);
        command.setDescription(DESCRIPTION);
        command.setUom(uomCommand);

        //then
        Ingredient ingredient = ingredientConverter.convert(command);

        assertNotNull(ingredient);
        assertNotNull(ingredient.getUom());
        assertEquals(ingredient.getUom().getId(), 2L);
        assertEquals(ID, ingredient.getId());

    }
}