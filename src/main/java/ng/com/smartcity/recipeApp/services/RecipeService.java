package ng.com.smartcity.recipeApp.services;

import ng.com.smartcity.recipeApp.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();
}
