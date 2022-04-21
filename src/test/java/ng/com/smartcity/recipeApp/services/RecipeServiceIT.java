package ng.com.smartcity.recipeApp.services;

import ng.com.smartcity.recipeApp.commands.RecipeCommand;
import ng.com.smartcity.recipeApp.converters.RecipeCommandToRecipe;
import ng.com.smartcity.recipeApp.converters.RecipeToRecipeCommand;
import ng.com.smartcity.recipeApp.domain.Recipe;
import ng.com.smartcity.recipeApp.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RecipeServiceIT {

    private static final String NEW_DESCRIPTION = "New Description";
    @Autowired
    RecipeService recipeService;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    RecipeCommandToRecipe commandConverter;
    @Autowired
    RecipeToRecipeCommand recipeConverter;


    @Transactional
    @Test
    void testSaveOfDescription() {
        //given
        Iterable<Recipe> recipes = recipeRepository.findAll();
        Recipe returnedRecipe = recipes.iterator().next();
        RecipeCommand recipeCommand = recipeConverter.convert(returnedRecipe);

        //when
        recipeCommand.setDescription(NEW_DESCRIPTION);
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);

        //then
        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(returnedRecipe.getId(), savedRecipeCommand.getId());
        assertEquals(returnedRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());
        assertInstanceOf(RecipeCommand.class, savedRecipeCommand);
    }
}
