package com.restApi.spring.service;

import com.restApi.spring.model.Beer;
import com.restApi.spring.model.Containers;

import java.util.List;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

public interface BeerService {
    Beer findById(Long id);
    Beer findByDescription(String description);

    Beer saveBeer(Beer beer);
    Beer updateBeer(Beer beer);
    void deleteBeerById(Beer beer);
    void deleteAllBeers();
    List<Beer> findAllBeers();
    List<Beer> findBeersByContainers(Containers containers);
    boolean isBeerExist(Beer beer);
    List<Beer> createBeersDefault(Containers containers);
}