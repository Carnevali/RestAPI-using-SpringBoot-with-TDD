package com.restApi.spring.service;

import com.restApi.spring.enums.StatusType;
import com.restApi.spring.model.Containers;
import com.restApi.spring.repositories.ContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    public List<Containers> findAllContainers(){
        return containerRepository.findAll();
    }

    public void deleteAllContainers(){
        containerRepository.deleteAll();
    }
    public void deleteContainerById(Containers containers){
        containerRepository.delete(containers);
    }

    public List<Containers> createDefaultContainers(){
        deleteAllContainers();
        beerService.deleteAllBeers();

        List<Containers> result = new ArrayList<>();

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
