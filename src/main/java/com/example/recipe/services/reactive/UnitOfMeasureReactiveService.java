package com.example.recipe.services.reactive;

import com.example.recipe.commands.UnitOfMeasureCommand;
import com.example.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.example.recipe.domain.UnitOfMeasure;
import com.example.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import com.example.recipe.services.UnitOfMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UnitOfMeasureReactiveService implements UnitOfMeasureService {

    private final UnitOfMeasureReactiveRepository repository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    @Autowired
    public UnitOfMeasureReactiveService(UnitOfMeasureReactiveRepository repository, UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.repository = repository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    public Mono<UnitOfMeasure> save(UnitOfMeasure unitOfMeasure) {
        return this.repository.save(unitOfMeasure);
    }

    public Mono<UnitOfMeasure> getByDescription(String description) {
        return this.repository.findUnitOfMeasuresByDescription(description);
    }

    public void deleteByDescription(String description) {
        if (this.getByDescription(description).block() != null)
            this.repository.deleteUnitOfMeasureByDescription(description);
    }

    public Flux<UnitOfMeasure> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Flux<UnitOfMeasureCommand> listAllUoms() {
        return this.repository.findAll().map(this.unitOfMeasureToUnitOfMeasureCommand::convert);
    }

    @Override
    public Mono<UnitOfMeasure> findById(String id) {
        return this.repository.findById(id);
    }

}
