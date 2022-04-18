package ng.com.smartcity.recipeApp.converters;

import ng.com.smartcity.recipeApp.commands.IngredientCommand;
import ng.com.smartcity.recipeApp.domain.Ingredient;
import ng.com.smartcity.recipeApp.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientToIngredientCommandTest {

    private final static Long ID = 1L;
    private final static String DESCRIPTION = "description";
    private final static BigDecimal AMOUNT = new BigDecimal(1);
    static UnitOfMeasure uom;
    IngredientToIngredientCommand converter;

    @BeforeEach
    void setUp() {
        uom = new UnitOfMeasure();
        uom.setId(2L);
        uom.setDescription(DESCRIPTION);
        converter = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    void convert() {
        //given
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ID);
        ingredient.setDescription(DESCRIPTION);
        ingredient.setAmount(AMOUNT);
        ingredient.setUom(uom);

        //then
        IngredientCommand command = converter.convert(ingredient);

        assertNotNull(command);
        assertNotNull(command.getUnitOfMeasure());
        assertEquals(ID, command.getId());
    }
}