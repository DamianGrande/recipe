package com.example.recipe.repositories;

import com.example.recipe.domain.UnitOfMeasure;
import com.example.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest
class UnitOfMeasureRepositoryTest {

    @Autowired
    private UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    @Test
    void findByDescription() {
        Mono<UnitOfMeasure> uomOptional = this.unitOfMeasureRepository.findUnitOfMeasuresByDescription("Teaspoon");
        Assertions.assertEquals("Teaspoon", uomOptional.block().getDescription());
    }

    @Test
    void findByDescriptionCup() {
        Mono<UnitOfMeasure> uomOptional = this.unitOfMeasureRepository.findUnitOfMeasuresByDescription("Cup");
        Assertions.assertEquals("Cup", uomOptional.block().getDescription());
    }

}