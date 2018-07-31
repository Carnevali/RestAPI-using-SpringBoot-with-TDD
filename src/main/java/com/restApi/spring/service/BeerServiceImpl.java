package com.restApi.spring.service;

import com.restApi.spring.enums.BeerType;
import com.restApi.spring.model.Beer;
import com.restApi.spring.model.Containers;
import com.restApi.spring.repositories.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

@Service("beerService")
@Transactional
public class BeerServiceImpl implements BeerService {

    @Autowired
    private BeerRepository beerRepository;

    public Beer findById(Long id) {
        Optional<Beer> beerDB = beerRepository.findById(id);

        if (beerDB.isPresent()) {
            return beerDB.get();
        }

        return null;
    }

    public Beer findByDescription(String description) {
        return beerRepository.findByDescription(description);
    }

    public Beer saveBeer(Beer beer) {
        return beerRepository.save(beer);
    }

    public Beer updateBeer(Beer beer){
        return saveBeer(beer);
    }

    public void deleteBeerById(Beer beer){
        beerRepository.delete(beer);
    }

    public void deleteAllBeers(){
        beerRepository.deleteAll();
    }

    public List<Beer> findAllBeers(){
        return beerRepository.findAll();
    }

    public List<Beer> findBeersByContainers(Containers containers){
        return beerRepository.findBeersByContainers(containers);
    }

    public boolean isBeerExist(Beer beer) {
        return findByDescription(beer.getDescription()) != null;
    }

    public void createBeersDefault() {
        deleteAllBeers();

        createBeerInternal(BeerType.PILSNER, 4.0, 6.0, "Beer 1 (Pilsner)", 0);
        createBeerInternal(BeerType.IPA, 5.0, 6.0, "Beer 2 (IPA)", 0);
        createBeerInternal(BeerType.LARGER, 4.0, 7.0, "Beer 3 (Larger)", 0);
        createBeerInternal(BeerType.STOUT, 6.0, 8.0, "Beer 4 (Stout)", 1);
        createBeerInternal(BeerType.WHEATBEER, 3.0, 5.0, "Beer 5 (Wheat beer)", 0);
        createBeerInternal(BeerType.PALEALE, 4.0, 6.0, "Beer 6 (Pale Ale)", 0);
    }

    private void createBeerInternal(BeerType beerType, Double min, Double max, String description, Integer status) {
        Beer beer = new Beer();
        beer.setType(beerType);
        beer.setMin(BigDecimal.valueOf(min));
        beer.setMax(BigDecimal.valueOf(max));
        beer.setDescription(description);
        beer.setStatus(status);

        saveBeer(beer);
    }

}
