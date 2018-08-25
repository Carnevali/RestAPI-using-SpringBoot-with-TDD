package com.restApi.spring.service;

import com.restApi.spring.model.Containers;
import java.util.List;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

public interface ContainerService {
    Containers findById(Long id);
    Containers findByDescription(String description);
    Containers saveContainer(Containers containers);
    Containers updateContainer(Containers containers);
    void deleteAllContainers();
    void deleteContainerById(Containers containers);
    List<Containers> createDefaultContainers();
    List<Containers> findAllContainers();

    boolean isContainerExist(Containers containers);
}