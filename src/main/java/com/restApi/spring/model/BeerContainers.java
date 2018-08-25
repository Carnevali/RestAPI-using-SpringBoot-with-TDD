package com.restApi.spring.model;

import com.restApi.spring.enums.StatusType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by felipe on 25/8/18.
 */
@Entity
@Table(name = "beer_containers")
@IdClass(BeerContainersPK.class)
public class BeerContainers implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "beer_id", referencedColumnName = "id")
    private Beer beer;

    @Id
    @ManyToOne
    @JoinColumn(name = "containers_id", referencedColumnName = "id")
    private Containers containers;

    @JoinColumn(name = "status_beer")
    private StatusType statusBeer;

    public BeerContainers() {

    }

    public BeerContainers(Beer beer, Containers containers) {
        super();

        this.beer = beer;
        this.containers = containers;

        if (containers.getTemperature() > beer.getMax()) {
            this.statusBeer = StatusType.WARNING;
        } else if (containers.getTemperature() < beer.getMin()) {
            this.statusBeer = StatusType.WARNING;
        } else {
            this.statusBeer = StatusType.OK;
        }
    }

    public Beer getBeer() {
        return beer;
    }

    public Containers getContainers() {
        return containers;
    }

    public StatusType getStatusBeer() {
        return statusBeer;
    }

    public void updateStatusBeer() {
        if (this.containers.getTemperature() > this.beer.getMax()) {
            this.statusBeer = StatusType.WARNING;
        } else if (this.containers.getTemperature() < this.beer.getMin()) {
            this.statusBeer = StatusType.WARNING;
        } else {
            this.statusBeer = StatusType.OK;
        }
    }
}