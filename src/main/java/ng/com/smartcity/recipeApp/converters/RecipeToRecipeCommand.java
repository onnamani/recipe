package ng.com.smartcity.recipeApp.converters;

import ng.com.smartcity.recipeApp.commands.RecipeCommand;
import ng.com.smartcity.recipeApp.domain.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {

    private final IngredientToIngredientCommand ingredientConverter;
    private final NotesToNotesCommand notesConverter;
    private final CategoryToCategoryCommand categoryConverter;

    public RecipeToRecipeCommand(IngredientToIngredientCommand ingredientConverter,
                                 NotesToNotesCommand notesConverter,
                                 CategoryToCategoryCommand categoryConverter
                                 ) {
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
        this.categoryConverter = categoryConverter;
    }

    @Override
    public RecipeCommand convert(Recipe recipeSource) {
        if(recipeSource == null)
            return null;

        RecipeCommand command = new RecipeCommand();
        command.setId(recipeSource.getId());
        command.setDescription(recipeSource.getDescription());
        command.setPrepTime(recipeSource.getPrepTime());
        command.setCookTime(recipeSource.getCookTime());
        command.setServings(recipeSource.getServings());
        command.setSource(recipeSource.getSource());
        command.setUrl(recipeSource.getUrl());
        command.setDirections(recipeSource.getDirections());
        command.setIngredients(recipeSource.getIngredients().stream()
                        .map(ingredientConverter::convert)
                        .collect(Collectors.toSet())
        );
        command.setDifficulty(recipeSource.getDifficulty());
        command.setNotes(notesConverter.convert(recipeSource.getNotes()));
        command.setCategories(recipeSource.getCategories().stream()
                .map(categoryConverter::convert)
                .collect(Collectors.toSet())
        );
        command.setImage(recipeSource.getImage());

        return command;
    }
}
