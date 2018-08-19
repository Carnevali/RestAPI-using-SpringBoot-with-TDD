package com.restApi.spring.service;

import com.restApi.spring.model.Beer;
import com.restApi.spring.model.Containers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public void startSensor() {
        Runnable runnable = () -> changeBehavior();
        new Thread(runnable).start();
    }

    public void changeBehavior() {
        List<Containers> containersList = this.containerService.findAllContainers();

        for (Containers containers: containersList) {
            if (containers.getTemperature().intValue() == 15) {
                containers.resetTemperature();
            } else {
                containers.updateTemperature();
            }

            for (Beer beer: containers.getBeers()) {
                beer.updateStatus(containers.getTemperature());
                beerService.updateBeer(beer);
            }

            containers.updateStatus();

            this.containerService.updateContainer(containers);
        }
    }
}
