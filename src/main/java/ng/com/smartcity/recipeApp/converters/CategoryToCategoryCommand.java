package ng.com.smartcity.recipeApp.converters;

import lombok.Synchronized;
import ng.com.smartcity.recipeApp.commands.CategoryCommand;
import ng.com.smartcity.recipeApp.domain.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand> {

    @Synchronized
    @Override
    public CategoryCommand convert(Category source) {
        if(source == null)
            return null;

        CategoryCommand command = new CategoryCommand();
        command.setId(source.getId());
        command.setDescription(source.getDescription());
        return command;
    }
}
