package ng.com.smartcity.recipeApp.controllers;

import ng.com.smartcity.recipeApp.domain.Category;
import ng.com.smartcity.recipeApp.domain.UnitOfMeasure;
import ng.com.smartcity.recipeApp.repositories.CategoryRepository;
import ng.com.smartcity.recipeApp.repositories.UnitOfMeasureRepository;
import ng.com.smartcity.recipeApp.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class IndexController {

    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/", "/index"})
    public String getIndexPage(Model model) {
        model.addAttribute("recipes", recipeService.getRecipes());
        return "index";
    }
}
