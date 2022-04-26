package ng.com.smartcity.recipeApp.services;

import lombok.extern.slf4j.Slf4j;
import ng.com.smartcity.recipeApp.commands.IngredientCommand;
import ng.com.smartcity.recipeApp.converters.IngredientCommandToIngredient;
import ng.com.smartcity.recipeApp.converters.IngredientToIngredientCommand;
import ng.com.smartcity.recipeApp.domain.Ingredient;
import ng.com.smartcity.recipeApp.domain.Recipe;
import ng.com.smartcity.recipeApp.repositories.RecipeRepository;
import ng.com.smartcity.recipeApp.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientConverter;
    private final IngredientCommandToIngredient ingredientCommandConverter;
    private final UnitOfMeasureRepository uomRepository;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientConverter, IngredientCommandToIngredient ingredientCommandConverter, UnitOfMeasureRepository uomRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientConverter = ingredientConverter;
        this.ingredientCommandConverter = ingredientCommandConverter;
        this.uomRepository = uomRepository;
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

    @Transactional
    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if(!recipeOptional.isPresent()) {
            log.error("Recipe not found for ID: " + command.getRecipeId());
            throw new RuntimeException("recipe not found for this ingredient ID: " + command.getId());
        }

        Recipe recipe = recipeOptional.get();

        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();

        if (ingredientOptional.isPresent()) {
            Ingredient ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(command.getDescription());
            ingredientFound.setAmount(command.getAmount());
            ingredientFound.setUom(
                    uomRepository.findById(command.getUom().getId())
                            .orElseThrow(() -> {
                                log.error("uom ID not found: " + command.getUom().getId());
                                return new RuntimeException("uom ID not found");
                            }));
        } else {
            recipe.addIngredient(ingredientCommandConverter.convert(command));
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        IngredientCommand savedIngredientCommand = savedRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .map(ingredientConverter::convert)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Something went wrong. Please try again"));

        return savedIngredientCommand;

    }


}
