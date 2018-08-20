package com.restApi.spring.controller;

import com.restApi.spring.model.Containers;
import com.restApi.spring.service.ContainerService;
import com.restApi.spring.service.SensorService;
import com.restApi.spring.util.CustomErrorType;
import com.restApi.spring.util.HttpReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Set;
import java.util.concurrent.CompletableFuture;


/**
 * Created by felipecarnevalli on 14/7/18.
 */

@RestController
@RequestMapping("/api/container")
public class ContainerAPIController {

    public static final Logger logger = LoggerFactory.getLogger(ContainerAPIController.class);

    @Autowired
    ContainerService containerService;

    @Autowired
    SensorService sensorService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Set<Containers>> listAllContainers() {
        Set<Containers> containerses = containerService.findAllContainers();
        if (containerses.isEmpty()) {
            return new ResponseEntity<Set<Containers>>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Set<Containers>>(containerses, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getContainer(@PathVariable("id") long id) {
        logger.info("Fetching containers with id {}", id);
        Containers containers = containerService.findById(id);

        if (containers == null) {
            logger.error("Containers with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Containers with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Containers>(containers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createContainer(@RequestBody Containers containers, UriComponentsBuilder ucBuilder) {
        logger.info("Creating Containers : {}", containers);

        if (containerService.isContainerExist(containers)) {
            logger.error("Unable to create. A Containers with id {} already exist", containers.getId());
            return new ResponseEntity(new CustomErrorType("Unable to create. A Containers with id " +
                    containers.getId() + " already exist."), HttpStatus.CONFLICT);
        }

        containerService.saveContainer(containers);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/containers/{id}").buildAndExpand(containers.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateContainer(@PathVariable("id") long id, @RequestBody Containers containers) {
        logger.info("Updating Containers with id {}", id);

        Containers currentContainers = containerService.findById(id);

        if (currentContainers == null) {
            logger.error("Unable to update. Containers with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to upate. Containers with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        containerService.updateContainer(containers);
        return new ResponseEntity<Containers>(currentContainers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAllContainers() {
        HttpReturn ok = new HttpReturn();

        try {
            logger.info("Deleting All Containers");

            containerService.deleteAllContainers();
            ok.setSuccess(true);
            ok.setMessage("Works well");
            return new ResponseEntity<HttpReturn>(ok, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            ok.setSuccess(false);
            ok.setMessage(e.getMessage());
            return new ResponseEntity<HttpReturn>(ok, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/default", method = RequestMethod.GET)
    public ResponseEntity<?> createDefault() {
        Set<Containers> containers = containerService.createDefaultContainers();
        sensorService.startSensor();

        return new ResponseEntity<Set<Containers>>(containers, HttpStatus.CREATED);
    }

    @Async("changeBehavior")
    public CompletableFuture changeBehavior() {
        this.sensorService.changeBehavior();
        return CompletableFuture.completedFuture("change");
    }
}