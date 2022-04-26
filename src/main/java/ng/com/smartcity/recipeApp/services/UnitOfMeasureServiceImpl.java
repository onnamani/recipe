package ng.com.smartcity.recipeApp.services;

import ng.com.smartcity.recipeApp.commands.UnitOfMeasureCommand;
import ng.com.smartcity.recipeApp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import ng.com.smartcity.recipeApp.domain.UnitOfMeasure;
import ng.com.smartcity.recipeApp.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureRepository uomRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand uomConverter;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository uomRepository,
                                    UnitOfMeasureToUnitOfMeasureCommand uomConverter) {
        this.uomRepository = uomRepository;
        this.uomConverter = uomConverter;
    }

    @Override
    public Set<UnitOfMeasureCommand> listAllUoms() {
        Set<UnitOfMeasure> uomSet = new HashSet<>();
        uomRepository.findAll().iterator().forEachRemaining(uomSet::add);

        return uomSet.stream()
                .map(uomConverter::convert)
                .collect(Collectors.toSet());
    }
}
