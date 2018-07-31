package com.restApi.spring.service;

import com.restApi.spring.model.Containers;
import com.restApi.spring.repositories.ContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

@Service("containerService")
@Transactional
public class ContainerServiceImpl implements ContainerService {

    @Autowired
    private ContainerRepository containerRepository;

    public Containers findById(Long id) {
        Optional<Containers> container = containerRepository.findById(id);

        if (container.isPresent()) {
            return container.get();
        }

        return null;
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

    public boolean isContainerExist(Containers containers) {
        if (containers.getId() != null) {
            return findById(containers.getId()) != null;
        }

        return false;
    }

}
