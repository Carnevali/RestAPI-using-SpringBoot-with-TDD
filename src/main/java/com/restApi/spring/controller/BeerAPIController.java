package com.restApi.spring.controller;

import com.restApi.spring.model.Beer;
import com.restApi.spring.model.Containers;
import com.restApi.spring.service.BeerService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

@RestController
@RequestMapping("/api")
public class BeerAPIController {
    public static final Logger logger = LoggerFactory.getLogger(BeerAPIController.class);

    @Autowired
    BeerService beerService;

    @Autowired
    ContainerService containerService;

    @Autowired
    SensorService sensorService;

    @RequestMapping(value = "/beer/", method = RequestMethod.GET)
    public ResponseEntity<List<?>> listAllBeers() {
        List<Beer> beers = beerService.findAllBeers();
        if (beers.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<?>>(beers, HttpStatus.OK);
    }

    @RequestMapping(value = "/beer/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getBeer(@PathVariable("id") long id) {
        logger.info("Fetching beer with id {}", id);
        Beer beer = beerService.findById(id);

        if (beer == null) {
            logger.error("Beer with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Beer with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Beer>(beer, HttpStatus.OK);
    }

    @RequestMapping(value = "/beer/", method = RequestMethod.POST)
    public ResponseEntity<?> createBeer(@RequestBody Beer beer, UriComponentsBuilder ucBuilder) {
        logger.info("Creating Beer : {}", beer);

        if (beerService.isBeerExist(beer)) {
            logger.error("Unable to create. A Beer with description {} already exist", beer.getDescription());
            return new ResponseEntity(new CustomErrorType("Unable to create. A Beer with description " +
                    beer.getDescription() + " already exist."), HttpStatus.CONFLICT);
        }

        beerService.saveBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/beer/{id}").buildAndExpand(beer.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/beer/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateBeer(@PathVariable("id") long id, @RequestBody Beer beer) {
        logger.info("Updating Beer with id {}", id);

        Beer currentBeer = beerService.findById(id);

        if (currentBeer == null) {
            logger.error("Unable to update. Beer with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to upate. Beer with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        currentBeer.setDescription(beer.getDescription());
        currentBeer.setMax(beer.getMax());
        currentBeer.setMin(beer.getMin());
        currentBeer.setType(beer.getType());
        currentBeer.setStatus(0);

        beerService.updateBeer(currentBeer);
        return new ResponseEntity<Beer>(currentBeer, HttpStatus.OK);
    }

    @RequestMapping(value = "/beer/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBeer(@PathVariable("id") long id) {
        logger.info("Fetching & Deleting Beer with id {}", id);

        Beer beer = beerService.findById(id);
        if (beer == null) {
            logger.error("Unable to delete. Beer with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to delete. Beer with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        HttpReturn ok = new HttpReturn();

        try {
            beerService.deleteBeerById(beer);
            ok.setSuccess(true);
            ok.setMessage("Works well");
            return new ResponseEntity<HttpReturn>(ok, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            ok.setSuccess(false);
            ok.setMessage(e.getMessage());
            return new ResponseEntity<HttpReturn>(ok, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/beer/", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAllBeers() {
        HttpReturn ok = new HttpReturn();

        try {
            logger.info("Deleting All Beers");

            beerService.deleteAllBeers();

            ok.setSuccess(true);
            ok.setMessage("Works well");
            return new ResponseEntity<HttpReturn>(ok, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            ok.setSuccess(false);
            ok.setMessage(e.getMessage());
            return new ResponseEntity<HttpReturn>(ok, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/beer/default/", method = RequestMethod.GET)
    public ResponseEntity<?> createDefault() {
        logger.info("Create Default Beers");

        containerService.deleteAllContainers();
        beerService.createBeersDefault();

        Containers containers = new Containers();
        containers.setDescription("Containers 1");
        containers.setTemperature(BigDecimal.valueOf(5.0));
        containers.setStatus(1);
        containerService.saveContainer(containers);

        List<Beer> beerList = beerService.findAllBeers();

        for (Beer b: beerList) {
            b.setContainers(containers);
            beerService.updateBeer(b);
        }

        containers.setBeers(beerList);
        containerService.updateContainer(containers);

        long TIME = (1000 * 8);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    Thread thread = new Thread(BeerAPIController.this::changeTemperature);
                    thread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        timer.scheduleAtFixedRate(task, TIME, TIME);

        return new ResponseEntity<Containers>(containers, HttpStatus.CREATED);
    }

    @Async("changeTemperature")
    public CompletableFuture changeTemperature() {
        this.sensorService.changeTemperature();
        return CompletableFuture.completedFuture("change");
    }

}