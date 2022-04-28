package ng.com.smartcity.recipeApp.controllers;

import lombok.extern.slf4j.Slf4j;
import ng.com.smartcity.recipeApp.commands.IngredientCommand;
import ng.com.smartcity.recipeApp.commands.RecipeCommand;
import ng.com.smartcity.recipeApp.commands.UnitOfMeasureCommand;
import ng.com.smartcity.recipeApp.services.IngredientService;
import ng.com.smartcity.recipeApp.services.RecipeService;
import ng.com.smartcity.recipeApp.services.UnitOfMeasureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService uomService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService uomService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.uomService = uomService;
    }

    @RequestMapping("/recipe/{recipeId}/ingredients")
    @GetMapping
    public String listIngredients(@PathVariable String recipeId, Model model) {
        log.debug("Getting ingredient for recipe id: " + recipeId);

        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));
        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeCommand.getId());
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", uomService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String getRecipeIngredient(@PathVariable String recipeId,
                                      @PathVariable String ingredientId,
                                      Model model) {
        model.addAttribute("ingredient",
                ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId)));
        return "recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String ingredientId,
                                         Model model) {
        model.addAttribute("ingredient",
                ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId)));

        model.addAttribute("uomList", uomService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable String recipeId,
                                 @PathVariable String ingredientId, Model model) {
        log.debug("deleting ingredient id: " + ingredientId);
        ingredientService.deleteIngredient(Long.valueOf(recipeId), Long.valueOf(ingredientId));

        return "redirect:/recipe/" + recipeId + "/ingredients";
    }

    @PostMapping
    @RequestMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
        IngredientCommand savedCommand = ingredientService.saveOrUpdateIngredientCommand(command);

        log.debug("saved recipe ID: " + savedCommand.getRecipeId());
        log.debug("saved ingredient ID: " + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }
}
