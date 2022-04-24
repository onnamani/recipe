package ng.com.smartcity.recipeApp.services;

import lombok.extern.slf4j.Slf4j;
import ng.com.smartcity.recipeApp.commands.IngredientCommand;
import ng.com.smartcity.recipeApp.converters.IngredientToIngredientCommand;
import ng.com.smartcity.recipeApp.domain.Recipe;
import ng.com.smartcity.recipeApp.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientConverter;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientConverter) {
        this.recipeRepository = recipeRepository;
        this.ingredientConverter = ingredientConverter;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        Recipe recipe = recipeOptional.orElseThrow(() -> {
            log.error("recipe ID not found");
            return new RuntimeException("recipe ID not found");
        });

        IngredientCommand ingredientCommand = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientConverter::convert)
                .findFirst()
                .orElseThrow(() -> {
                    log.error("ingredient ID not found");
                    return new RuntimeException("ingredient ID not found");
                });

        return ingredientCommand;
    }
}
