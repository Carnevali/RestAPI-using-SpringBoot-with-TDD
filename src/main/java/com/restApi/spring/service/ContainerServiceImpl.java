package com.restApi.spring.service;

import com.restApi.spring.enums.StatusType;
import com.restApi.spring.model.Containers;
import com.restApi.spring.repositories.ContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

@Service("containerService")
@Transactional
public class ContainerServiceImpl implements ContainerService {

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private BeerService beerService;

    public Containers findById(Long id) {
        return containerRepository.findById(id).orElse(null);
    }

    public Containers findByDescription(String description) {
        return containerRepository.findByDescription(description);
    }

    public Containers saveContainer(Containers containers) {
        return containerRepository.save(containers);
    }

    public Containers updateContainer(Containers containers){
        return saveContainer(containers);
    }

    public Set<Containers> findAllContainers(){
        return new HashSet(containerRepository.findAll());
    }

    public void deleteAllContainers(){
        containerRepository.deleteAll();
    }

    public Set<Containers> createDefaultContainers(){
        deleteAllContainers();
        beerService.deleteAllBeers();

        Set<Containers> result = new HashSet<>();

        Containers containers = new Containers("Containers 1", 5.0, beerService.createBeersDefault(), StatusType.WARNING);
        result.add(saveContainer(containers));

        containers = new Containers("Containers 2", 3.0, beerService.createBeersDefault(), StatusType.OK);
        result.add(saveContainer(containers));

        containers = new Containers("Containers 3", 6.0, beerService.createBeersDefault(), StatusType.OK);
        result.add(saveContainer(containers));

        return findAllContainers();
    }

    public boolean isContainerExist(Containers containers) {
        if (containers.getId() != null) {
            return findById(containers.getId()) != null;
        }

        return false;
    }

}
