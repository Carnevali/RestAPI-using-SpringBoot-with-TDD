package com.restApi.spring.repositories;

import com.restApi.spring.model.Beer;
import com.restApi.spring.model.BeerContainers;
import com.restApi.spring.model.Containers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by felipecarnevalli on 25/8/18.
 */

@Repository
public interface BeerContainersRepository extends JpaRepository<BeerContainers, Long> {
    BeerContainers findByBeerId(Beer beer);
    BeerContainers findByContainersId(Containers containers);
}