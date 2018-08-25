package com.restApi.spring.service;

import com.restApi.spring.model.Beer;
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
    boolean isBeerExist(Beer beer);
    List<Beer> createBeersDefault();
}