package com.example.recipe.services;

import com.example.recipe.commands.UnitOfMeasureCommand;
import com.example.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.example.recipe.domain.UnitOfMeasure;
import com.example.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import com.example.recipe.services.reactive.UnitOfMeasureReactiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnitOfMeasureServiceImplTest {
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
    UnitOfMeasureService service;

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.service = new UnitOfMeasureReactiveService(this.unitOfMeasureRepository, this.unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    void listAllUoms() {

        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId("1");
        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId("2");

        when(this.unitOfMeasureRepository.findAll()).thenReturn(Flux.just(uom1, uom2));

        List<UnitOfMeasureCommand> commands = this.service.listAllUoms().collectList().block();

        assertEquals(2, commands.size());
        verify(this.unitOfMeasureRepository, times(1)).findAll();

    }
}