package com.example.recipe.services;

import com.example.recipe.commands.UnitOfMeasureCommand;
import com.example.recipe.domain.UnitOfMeasure;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UnitOfMeasureService {

    Flux<UnitOfMeasureCommand> listAllUoms();

    Mono<UnitOfMeasure> findById(String id);

}
