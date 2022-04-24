package ng.com.smartcity.recipeApp.converters;

import lombok.Synchronized;
import ng.com.smartcity.recipeApp.commands.IngredientCommand;
import ng.com.smartcity.recipeApp.domain.Ingredient;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {

    private final UnitOfMeasureToUnitOfMeasureCommand uomConverter;

    public IngredientToIngredientCommand(UnitOfMeasureToUnitOfMeasureCommand uomConverter) {
        this.uomConverter = uomConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public IngredientCommand convert(Ingredient source) {
        if(source == null)
            return null;

        IngredientCommand command = new IngredientCommand();
        command.setId(source.getId());
        if(source.getRecipe() != null)
            command.setRecipeId(source.getRecipe().getId());
        command.setDescription(source.getDescription());
        command.setAmount(source.getAmount());
        command.setUom(uomConverter.convert(source.getUom()));
        return command;
    }
}
