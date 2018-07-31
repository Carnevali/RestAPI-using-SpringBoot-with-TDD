package com.restApi.spring.repositories;

import com.restApi.spring.model.Containers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

@Repository
public interface ContainerRepository extends JpaRepository<Containers, Long> {
    Containers findByDescription(String description);
}