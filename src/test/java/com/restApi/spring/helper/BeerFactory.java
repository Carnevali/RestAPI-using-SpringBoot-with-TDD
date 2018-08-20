package com.restApi.spring.helper;

import com.restApi.spring.enums.BeerType;
import com.restApi.spring.enums.StatusType;
import com.restApi.spring.model.Beer;
import com.restApi.spring.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Set;

/**
 * Created by felipe on 19/8/18.
 */
public class BeerFactory {
    @Autowired
    private BeerService beerService;

    public void createBeers(Set<Beer> beerList) {
        beerList.add(createBeerInternal(BeerType.PILSNER, 4.0, 6.0, "Beer 1 (Pilsner)", StatusType.OK));
        beerList.add(createBeerInternal(BeerType.IPA, 5.0, 6.0, "Beer 2 (IPA)", StatusType.OK));
        beerList.add(createBeerInternal(BeerType.LARGER, 4.0, 7.0, "Beer 3 (Larger)", StatusType.OK));
        beerList.add(createBeerInternal(BeerType.STOUT, 6.0, 8.0, "Beer 4 (Stout)", StatusType.WARNING));
        beerList.add(createBeerInternal(BeerType.WHEATBEER, 3.0, 5.0, "Beer 5 (Wheat beer)", StatusType.OK));
        beerList.add(createBeerInternal(BeerType.PALEALE, 4.0, 6.0, "Beer 6 (Pale Ale)", StatusType.OK));
    }

    private Beer createBeerInternal(BeerType beerType, Double min, Double max, String description, StatusType status) {
        Beer beer = new Beer(description, beerType, min, max, null, status);
        return beerService.saveBeer(beer);
    }
}
