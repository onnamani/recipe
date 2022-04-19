package ng.com.smartcity.recipeApp.converters;

import lombok.Synchronized;
import ng.com.smartcity.recipeApp.commands.CategoryCommand;
import ng.com.smartcity.recipeApp.domain.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category> {

    @Synchronized
    @Override
    public Category convert(CategoryCommand source) {
        if(source == null)
            return null;

        Category category = new Category();
        category.setId(source.getId());
        category.setDescription(source.getDescription());
        return category;
    }
}
