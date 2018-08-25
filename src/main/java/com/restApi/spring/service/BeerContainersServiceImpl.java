package com.restApi.spring.service;

import com.restApi.spring.model.Beer;
import com.restApi.spring.model.BeerContainers;
import com.restApi.spring.model.Containers;
import com.restApi.spring.repositories.BeerContainersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by felipecarnevalli on 25/8/18.
 */

@Service("beerContainersService")
@Transactional
public class BeerContainersServiceImpl implements BeerContainersService {

    @Autowired
    private BeerContainersRepository beerContainersRepository;

    public BeerContainers findById(Long id) {
        return beerContainersRepository.findById(id).orElse(null);
    }

    public BeerContainers save(BeerContainers beerContainers) {
        return beerContainersRepository.save(beerContainers);
    }

    public BeerContainers update(BeerContainers beerContainers){
        return save(beerContainers);
    }

    public BeerContainers findByContainersId(Containers containers) {
        return beerContainersRepository.findByContainersId(containers);
    }

    public BeerContainers findByBeerId(Beer beer) {
        return beerContainersRepository.findByBeerId(beer);

    }


}
