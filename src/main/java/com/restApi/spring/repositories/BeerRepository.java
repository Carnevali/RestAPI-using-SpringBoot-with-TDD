package com.restApi.spring.repositories;

import com.restApi.spring.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

@Repository
public interface BeerRepository extends JpaRepository<Beer, Long> {
    Beer findByDescription(String description);
}