package com.restApi.spring.controller;
import com.restApi.spring.model.Beer;
import com.restApi.spring.service.BeerService;
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
@RequestMapping("/api/beer")
public class BeerAPIController {
    public static final Logger logger = LoggerFactory.getLogger(BeerAPIController.class);

    @Autowired
    BeerService beerService;

    @Autowired
    ContainerService containerService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<?>> listAllBeers() {
        List<Beer> beers = beerService.findAllBeers();

        if (beers.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<?>>(beers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
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

    @RequestMapping(method = RequestMethod.POST)
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

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateBeer(@PathVariable("id") long id, @RequestBody Beer beer) {
        logger.info("Updating Beer with id {}", id);

        Beer currentBeer = beerService.findById(id);

        if (currentBeer == null) {
            logger.error("Unable to update. Beer with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to update. Beer with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        beerService.updateBeer(beer);
        return new ResponseEntity<Beer>(currentBeer, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
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

    @RequestMapping(method = RequestMethod.DELETE)
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
}