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
    public IngredientCommand saveOrUpdateIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        Recipe recipe = recipeOptional.orElseThrow(() -> {
            log.error("Recipe not found for ID: " + command.getRecipeId());
            return new RuntimeException("recipe not found for this ingredient");
        });

        Ingredient saveOrUpdateIngredient;

        if(recipe.getIngredients().stream().
                filter(ingredient -> ingredient.getId().equals(command.getId())).findFirst().isPresent()) {
            saveOrUpdateIngredient = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst()
                    .orElseThrow(() -> {
                        log.error("ingredient for id: " + command.getId() + " not returned from recipe");
                        return new RuntimeException("Something went wrong during update. Try again");
                    });

            saveOrUpdateIngredient.setDescription(command.getDescription());
            saveOrUpdateIngredient.setAmount(command.getAmount());
            saveOrUpdateIngredient.setUom(uomRepository.findById(command.getUom().getId())
                    .orElseThrow(() -> {
                        log.error("uom for the ingredient could not be found");
                        return new RuntimeException("Something went wrong while updating the unit of measure");
                    })
            );
        } else {
            saveOrUpdateIngredient = ingredientCommandConverter.convert(command);
        }

        recipe.addIngredient(saveOrUpdateIngredient);

        Recipe returnedRecipe = recipeRepository.save(recipe);

        IngredientCommand returnedIngredient = returnedRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getDescription().equals(command.getDescription())
                                            && ingredient.getAmount().equals(command.getAmount())
                                            && ingredient.getUom().getId().equals(command.getUom().getId()))
                .map(ingredientConverter::convert)
                .findFirst()
                .orElseThrow(() -> {
                    log.error("ingredient not returned from database recipe");
                    return new RuntimeException("Something went wrong while saving ingredient. Try again");
                });

        return returnedIngredient;

    }


}
