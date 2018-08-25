package com.restApi.spring.service;

import com.restApi.spring.enums.BeerType;
import com.restApi.spring.model.Beer;
import com.restApi.spring.repositories.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

@Service("beerService")
@Transactional
public class BeerServiceImpl implements BeerService {

    @Autowired
    private BeerRepository beerRepository;

    public Beer findById(Long id) {
        return beerRepository.findById(id).orElse(null);
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

    public boolean isBeerExist(Beer beer) {
        return findByDescription(beer.getDescription()) != null;
    }

    public List<Beer> createBeersDefault() {
        List<Beer> list = new ArrayList<>();

        list.add(createBeerInternal(BeerType.PILSNER, 4.0, 6.0, "Beer 1 (Pilsner)"));
        list.add(createBeerInternal(BeerType.IPA, 5.0, 6.0, "Beer 2 (IPA)"));
        list.add(createBeerInternal(BeerType.LARGER, 4.0, 7.0, "Beer 3 (Larger)"));
        list.add(createBeerInternal(BeerType.STOUT, 6.0, 8.0, "Beer 4 (Stout)"));
        list.add(createBeerInternal(BeerType.WHEATBEER, 3.0, 5.0, "Beer 5 (Wheat beer)"));
        list.add(createBeerInternal(BeerType.PALEALE, 4.0, 6.0, "Beer 6 (Pale Ale)"));

        return list;
    }

    private Beer createBeerInternal(BeerType beerType, Double min, Double max, String description) {
        Beer beer = new Beer(description, beerType, min, max);
        return saveBeer(beer);
    }

}
