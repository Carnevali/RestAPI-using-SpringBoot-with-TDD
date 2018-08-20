package com.restApi.spring.service;

import com.restApi.spring.model.Beer;
import com.restApi.spring.model.Containers;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Set;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

public interface BeerService {
    @EntityGraph(attributePaths = "containers")
    Beer findById(Long id);

    @EntityGraph(attributePaths = "containers")
    Beer findByDescription(String description);

    Beer saveBeer(Beer beer);
    Beer updateBeer(Beer beer);
    void deleteBeerById(Beer beer);
    void deleteAllBeers();

    @EntityGraph(attributePaths = "containers")
    Set<Beer> findAllBeers();

    @EntityGraph(attributePaths = "containers")
    Set<Beer> findBeersByContainers(Containers containers);

    boolean isBeerExist(Beer beer);
    Set<Beer> createBeersDefault();
}