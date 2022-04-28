package ng.com.smartcity.recipeApp.services;

import ng.com.smartcity.recipeApp.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientCommand saveOrUpdateIngredientCommand(IngredientCommand command);
}
