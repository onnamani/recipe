package ng.com.smartcity.recipeApp.services;

import ng.com.smartcity.recipeApp.commands.UnitOfMeasureCommand;
import ng.com.smartcity.recipeApp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import ng.com.smartcity.recipeApp.domain.UnitOfMeasure;
import ng.com.smartcity.recipeApp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnitOfMeasureServiceImplTest {

    @Mock
    UnitOfMeasureRepository uomRepository;

    UnitOfMeasureToUnitOfMeasureCommand uomConverter;
    UnitOfMeasureServiceImpl uomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        uomConverter = new UnitOfMeasureToUnitOfMeasureCommand();
        uomService = new UnitOfMeasureServiceImpl(uomRepository, uomConverter);
    }

    @Test
    void listAllUoms() {
        //given
        Set<UnitOfMeasure> uomSet = new HashSet<>();

        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId(1L);
        uomSet.add(uom1);

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId(2L);
        uomSet.add(uom2);

        //when
        when(uomRepository.findAll()).thenReturn(uomSet);

        //then
        assertEquals(uomService.listAllUoms().size(), 2);
        assertInstanceOf(UnitOfMeasureCommand.class, uomService.listAllUoms().stream().findFirst().get());

        verify(uomRepository, times(2)).findAll();
    }
}