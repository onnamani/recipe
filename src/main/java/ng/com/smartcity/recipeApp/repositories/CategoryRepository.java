package ng.com.smartcity.recipeApp.repositories;

import ng.com.smartcity.recipeApp.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
