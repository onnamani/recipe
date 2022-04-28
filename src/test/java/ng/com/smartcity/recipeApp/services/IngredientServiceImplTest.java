package ng.com.smartcity.recipeApp.services;

import ng.com.smartcity.recipeApp.commands.IngredientCommand;
import ng.com.smartcity.recipeApp.commands.UnitOfMeasureCommand;
import ng.com.smartcity.recipeApp.converters.IngredientCommandToIngredient;
import ng.com.smartcity.recipeApp.converters.IngredientToIngredientCommand;
import ng.com.smartcity.recipeApp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import ng.com.smartcity.recipeApp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import ng.com.smartcity.recipeApp.domain.Ingredient;
import ng.com.smartcity.recipeApp.domain.Recipe;
import ng.com.smartcity.recipeApp.repositories.RecipeRepository;
import ng.com.smartcity.recipeApp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;
    @Mock
    UnitOfMeasureRepository uomRepository;

    IngredientServiceImpl ingredientService;
    IngredientCommandToIngredient ingredientCommandConverter;
    IngredientToIngredientCommand ingredientConverter;
    UnitOfMeasureCommandToUnitOfMeasure uomCommandConverter;
    UnitOfMeasureToUnitOfMeasureCommand uomConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        uomConverter = new UnitOfMeasureToUnitOfMeasureCommand();
        uomCommandConverter = new UnitOfMeasureCommandToUnitOfMeasure();
        ingredientConverter = new IngredientToIngredientCommand(uomConverter);
        ingredientCommandConverter = new IngredientCommandToIngredient(uomCommandConverter);
        ingredientService = new IngredientServiceImpl(recipeRepository,
                ingredientConverter,
                ingredientCommandConverter,
                uomRepository);
    }

    @Test
    void findByRecipeIdAndIngredientId() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(3L);

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        //when
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        //then
        assertInstanceOf(IngredientCommand.class,
                ingredientService.findByRecipeIdAndIngredientId(1L, 2L));
        assertEquals(ingredientService.findByRecipeIdAndIngredientId(1L, 3L).getId(), 3L);
        verify(recipeRepository, times(2)).findById(anyLong());

    }

    @Test
    void saveIngredientCommandHappyPath() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(1L);
        ingredientCommand.setAmount(new BigDecimal(20));
        ingredientCommand.setDescription("new description");

        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(3L);
        ingredientCommand.setUom(uomCommand);

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        when(uomRepository.findById(anyLong())).thenReturn(Optional.of(uomCommandConverter.convert(uomCommand)));
        when(recipeRepository.save(any())).thenReturn(recipe);

        //when
        IngredientCommand savedIngredientCommand = ingredientService.saveOrUpdateIngredientCommand(ingredientCommand);

        //then
        assertNotNull(savedIngredientCommand);
        assertEquals(savedIngredientCommand.getRecipeId(), recipe.getId());
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, times(1)).save(any());
        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateIngredientCommandHappyPath() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(2L);
        ingredientCommand.setRecipeId(1L);
        ingredientCommand.setAmount(new BigDecimal(20));
        ingredientCommand.setDescription("new description");


        recipe.addIngredient(ingredientCommandConverter.convert(ingredientCommand));

        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(3L);
        ingredientCommand.setUom(uomCommand);

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        when(uomRepository.findById(anyLong())).thenReturn(Optional.of(uomCommandConverter.convert(uomCommand)));
        when(recipeRepository.save(any())).thenReturn(recipe);

        //when
        IngredientCommand savedIngredientCommand = ingredientService.saveOrUpdateIngredientCommand(ingredientCommand);

        //then
        assertEquals(savedIngredientCommand.getId(), 2L);
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, times(1)).save(any());
        verify(recipeRepository, times(1)).findById(anyLong());

    }

    @Test
    public void saveIngredientCommandWithNoRecipe() {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();

        //then
        assertThrows(RuntimeException.class, () -> {
            ingredientService.saveOrUpdateIngredientCommand(ingredientCommand);
        }, "recipe not found for this ingredient ID: " + ingredientCommand.getId());
    }
}