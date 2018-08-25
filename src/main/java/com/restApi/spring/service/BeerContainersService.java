package com.restApi.spring.service;

import com.restApi.spring.model.Beer;
import com.restApi.spring.model.BeerContainers;
import com.restApi.spring.model.Containers;

/**
 * Created by felipecarnevalli on 25/8/18.
 */

public interface BeerContainersService {
    BeerContainers findById(Long id);
    BeerContainers findByContainersId(Containers containers);
    BeerContainers findByBeerId(Beer beer);
    BeerContainers save(BeerContainers beerContainers);
    BeerContainers update(BeerContainers beerContainers);
}