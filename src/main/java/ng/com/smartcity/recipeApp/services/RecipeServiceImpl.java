package ng.com.smartcity.recipeApp.services;

import lombok.extern.slf4j.Slf4j;
import ng.com.smartcity.recipeApp.commands.RecipeCommand;
import ng.com.smartcity.recipeApp.converters.RecipeCommandToRecipe;
import ng.com.smartcity.recipeApp.converters.RecipeToRecipeCommand;
import ng.com.smartcity.recipeApp.domain.Recipe;
import ng.com.smartcity.recipeApp.exceptions.NotFoundException;
import ng.com.smartcity.recipeApp.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe commandConverter;
    private final RecipeToRecipeCommand recipeConverter;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeCommandToRecipe commandConverter,
                             RecipeToRecipeCommand recipeConverter) {
        this.recipeRepository = recipeRepository;
        this.commandConverter = commandConverter;
        this.recipeConverter = recipeConverter;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("I'm in the service");

        Set<Recipe> recipeSet = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);

        return recipeOptional.orElseThrow(() -> new NotFoundException("Recipe Not Found"));
    }

    @Transactional
    @Override
    public RecipeCommand findCommandById(Long id) {
        return recipeConverter.convert(findById(id));
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {

        Recipe pojoRecipe = commandConverter.convert(recipeCommand);

        Recipe savedRecipe = recipeRepository.save(pojoRecipe);
        log.debug("Saved RecipeId: " + savedRecipe.getId());

        return recipeConverter.convert(savedRecipe);
    }

}
