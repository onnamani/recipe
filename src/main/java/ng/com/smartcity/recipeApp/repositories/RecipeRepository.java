package ng.com.smartcity.recipeApp.repositories;

import ng.com.smartcity.recipeApp.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
