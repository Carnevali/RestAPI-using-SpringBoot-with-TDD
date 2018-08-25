package com.restApi.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.restApi.spring.enums.StatusType;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

@Entity
@Table(name="containers")
@JsonIgnoreProperties( ignoreUnknown = true )
public class Containers implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "temperature", nullable = false)
    private Double temperature;

    @OneToMany(mappedBy = "containers", fetch = FetchType.EAGER)
    private List<BeerContainers> beerContainers = new ArrayList<>();

    @Column(name = "status", nullable = false)
    private StatusType status = StatusType.OK;

    public Containers() {

    }

    public Containers(String description, Double temperature, StatusType status) {
        super();

        this.description = description;
        this.temperature = temperature;
        this.status = status;
    }

    public Containers(String description, Double temperature, List<BeerContainers> beerContainers, StatusType status) {
        super();

        this.description = description;
        this.temperature = temperature;
        this.beerContainers = beerContainers;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Double getTemperature() {
        return temperature;
    }

    public List<BeerContainers> getBeerContainers() {
        return beerContainers;
    }

    public StatusType getStatus() {
        return status;
    }

    public void updateTemperature() {
        this.temperature += 1.0;

        for (BeerContainers bc: this.beerContainers) {
            bc.getContainers().updateTemperature();
        }
    }

    public void resetTemperature() {
        this.temperature = 2.0;

        for (BeerContainers bc: this.beerContainers) {
            bc.getContainers().resetTemperature();
        }
    }

    public void updateStatus() {
        Set<Beer> goodBeers = new HashSet<>();
        Set<Beer> badBeers = new HashSet<>();
        Set<Beer> totalBeers = new HashSet<>();

        for (BeerContainers bc : this.beerContainers) {
            if (this.temperature > bc.getBeer().getMax()) {
                badBeers.add(bc.getBeer());
            } else if (this.temperature < bc.getBeer().getMin()) {
                badBeers.add(bc.getBeer());
            } else {
                goodBeers.add(bc.getBeer());
            }

            totalBeers.add(bc.getBeer());
        }

        if (totalBeers.containsAll(goodBeers)) {
            this.status = StatusType.OK;
        } else if (totalBeers.containsAll(badBeers)) {
            this.status = StatusType.DANGER;
        } else {
            this.status = StatusType.WARNING;
        }
    }
}