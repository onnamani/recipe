package ng.com.smartcity.recipeApp.services;

import ng.com.smartcity.recipeApp.commands.UnitOfMeasureCommand;

import java.util.Set;

public interface UnitOfMeasureService {
    Set<UnitOfMeasureCommand> listAllUoms();
}
