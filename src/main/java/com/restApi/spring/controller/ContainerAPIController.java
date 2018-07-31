package com.restApi.spring.controller;

import com.restApi.spring.model.Containers;
import com.restApi.spring.service.ContainerService;
import com.restApi.spring.util.CustomErrorType;
import com.restApi.spring.util.HttpReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


/**
 * Created by felipecarnevalli on 14/7/18.
 */

@RestController
@RequestMapping("/api")
public class ContainerAPIController {

    public static final Logger logger = LoggerFactory.getLogger(ContainerAPIController.class);

    @Autowired
    ContainerService containerService;

    @RequestMapping(value = "/container/", method = RequestMethod.GET)
    public ResponseEntity<List<?>> listAllContainers() {
        List<Containers> containerses = containerService.findAllContainers();
        if (containerses.isEmpty()) {
            return new ResponseEntity<List<?>>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<?>>(containerses, HttpStatus.OK);
    }

    @RequestMapping(value = "/container/{id}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/container/", method = RequestMethod.POST)
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

    @RequestMapping(value = "/container/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateContainer(@PathVariable("id") long id, @RequestBody Containers containers) {
        logger.info("Updating Containers with id {}", id);

        Containers currentContainers = containerService.findById(id);

        if (currentContainers == null) {
            logger.error("Unable to update. Containers with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to upate. Containers with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        currentContainers.setDescription(containers.getDescription());
        currentContainers.setTemperature(containers.getTemperature());
        currentContainers.setStatus(0);

        containerService.updateContainer(currentContainers);
        return new ResponseEntity<Containers>(currentContainers, HttpStatus.OK);
    }

    @RequestMapping(value = "/container/", method = RequestMethod.DELETE)
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
}