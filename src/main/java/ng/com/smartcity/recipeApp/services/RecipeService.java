package ng.com.smartcity.recipeApp.services;

import ng.com.smartcity.recipeApp.commands.RecipeCommand;
import ng.com.smartcity.recipeApp.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe findById(Long id);

    RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);
}
