package ng.com.smartcity.recipeApp.converters;

import ng.com.smartcity.recipeApp.commands.IngredientCommand;
import ng.com.smartcity.recipeApp.commands.RecipeCommand;
import ng.com.smartcity.recipeApp.domain.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

    private final IngredientCommandToIngredient ingredientConverter;
    private final NotesCommandToNotes notesConverter;
    private final CategoryCommandToCategory categoryConverter;

    public RecipeCommandToRecipe(IngredientCommandToIngredient ingredientConverter,
                                 NotesCommandToNotes notesConverter,
                                 CategoryCommandToCategory categoryConverter) {
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
        this.categoryConverter = categoryConverter;
    }

    @Override
    public Recipe convert(RecipeCommand commandSource) {
        if(commandSource == null)
            return null;

        Recipe recipe = new Recipe();
        recipe.setId(commandSource.getId());
        recipe.setDescription(commandSource.getDescription());
        recipe.setPrepTime(commandSource.getPrepTime());
        recipe.setCookTime(commandSource.getCookTime());
        recipe.setServings(commandSource.getServings());
        recipe.setSource(commandSource.getSource());
        recipe.setUrl(commandSource.getUrl());
        recipe.setDirections(commandSource.getDirections());
        recipe.setIngredients(
                commandSource.getIngredients().stream()
                        .map(ingredientConverter::convert)
                        .collect(Collectors.toSet())
        );
        recipe.setDifficulty(commandSource.getDifficulty());
        recipe.setNotes(notesConverter.convert(commandSource.getNotes()));
        recipe.setCategories(
                commandSource.getCategories().stream()
                        .filter(command -> command != null)
                        .map(categoryConverter::convert)
                        .collect(HashSet::new, HashSet::add, HashSet::addAll)
        );

        return recipe;
    }
}
