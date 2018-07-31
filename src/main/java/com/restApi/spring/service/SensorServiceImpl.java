package com.restApi.spring.service;

import com.restApi.spring.model.Beer;
import com.restApi.spring.model.Containers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by felipecarnevalli on 15/7/18.
 */

@Service("sensorService")
@Transactional
public class SensorServiceImpl implements SensorService {

    @Autowired
    private BeerService beerService;

    @Autowired
    private ContainerService containerService;

    public void changeTemperature() {
        List<Containers> containersList = this.containerService.findAllContainers();

        for (Containers containers: containersList) {
            if (containers.getTemperature().intValue() == 15) {
                containers.setTemperature(BigDecimal.valueOf(2.0));
            } else {
                containers.setTemperature(containers.getTemperature().add(BigDecimal.ONE));
            }

            containers.setBeers(beerService.findBeersByContainers(containers));
            Integer countBeers = 0;

            for (Beer beer: containers.getBeers()) {
                if (containers.getTemperature().intValue() > beer.getMax().intValue()) {
                    beer.setStatus(1);
                    countBeers += 1;

                } else if (containers.getTemperature().intValue() < beer.getMin().intValue()) {
                    beer.setStatus(1);
                    countBeers += 1;
                } else {
                    beer.setStatus(0);
                }

                beerService.updateBeer(beer);
            }

            if (countBeers == containers.getBeers().size()) {
                containers.setStatus(2);
            } else if (countBeers > 0) {
                containers.setStatus(1);
            } else {
                containers.setStatus(0);
            }

            this.containerService.updateContainer(containers);
        }
    }
}
