package com.restApi.spring.service;

import com.restApi.spring.model.Containers;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Set;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

public interface ContainerService {
    @EntityGraph(attributePaths = "beers")
    Containers findById(Long id);

    @EntityGraph(attributePaths = "beers")
    Containers findByDescription(String description);

    Containers saveContainer(Containers containers);
    Containers updateContainer(Containers containers);
    void deleteAllContainers();
    void deleteContainerById(Containers containers);
    List<Containers> createDefaultContainers();

    @EntityGraph(attributePaths = "beers")
    List<Containers> findAllContainers();

    boolean isContainerExist(Containers containers);
}