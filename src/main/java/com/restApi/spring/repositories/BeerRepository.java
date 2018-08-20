package com.restApi.spring.repositories;

import com.restApi.spring.model.Beer;
import com.restApi.spring.model.Containers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

@Repository
public interface BeerRepository extends JpaRepository<Beer, Long> {
    Beer findByDescription(String description);
    Set<Beer> findBeersByContainers(Containers container);
}