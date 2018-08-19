package com.restApi.spring.service;

import com.restApi.spring.enums.BeerType;
import com.restApi.spring.enums.StatusType;
import com.restApi.spring.model.Beer;
import com.restApi.spring.model.Containers;
import com.restApi.spring.repositories.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    public List<Beer> findBeersByContainers(Containers containers){
        return beerRepository.findBeersByContainers(containers);
    }

    public boolean isBeerExist(Beer beer) {
        return findByDescription(beer.getDescription()) != null;
    }

    public List<Beer> createBeersDefault() {
        List<Beer> list = new ArrayList<>();

        list.add(createBeerInternal(BeerType.PILSNER, 4.0, 6.0, "Beer 1 (Pilsner)", null, StatusType.OK));
        list.add(createBeerInternal(BeerType.IPA, 5.0, 6.0, "Beer 2 (IPA)", null, StatusType.OK));
        list.add(createBeerInternal(BeerType.LARGER, 4.0, 7.0, "Beer 3 (Larger)", null, StatusType.OK));
        list.add(createBeerInternal(BeerType.STOUT, 6.0, 8.0, "Beer 4 (Stout)", null, StatusType.WARNING));
        list.add(createBeerInternal(BeerType.WHEATBEER, 3.0, 5.0, "Beer 5 (Wheat beer)", null, StatusType.OK));
        list.add(createBeerInternal(BeerType.PALEALE, 4.0, 6.0, "Beer 6 (Pale Ale)", null, StatusType.OK));

        return list;
    }

    private Beer createBeerInternal(BeerType beerType, Double min, Double max, String description, List<Containers> containers, StatusType status) {
        Beer beer = new Beer(description, beerType, min, max, containers, status);
        return saveBeer(beer);
    }

}
