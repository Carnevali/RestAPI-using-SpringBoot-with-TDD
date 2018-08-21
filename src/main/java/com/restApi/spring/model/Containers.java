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
    private List<Beer> beers = new ArrayList<>();

    @Column(name = "status", nullable = false)
    private StatusType status = StatusType.OK;

    public Containers() {

    }

    public Containers(String description, Double temperature, List<Beer> beers, StatusType status) {
        super();

        this.description = description;
        this.temperature = temperature;
        this.beers = beers;
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

    public List<Beer> getBeers() {
        return beers;
    }

    public StatusType getStatus() {
        return status;
    }

    public void updateTemperature() {
        this.temperature += 1.0;
    }

    public void resetTemperature() {
        this.temperature = 2.0;
    }

    public void updateStatus() {
        Set<Beer> goodBeers = new HashSet<>();
        Set<Beer> badBeers = new HashSet<>();

        for (Beer beer : this.beers) {
            if (this.temperature > beer.getMax()) {
                badBeers.add(beer);
            } else if (this.temperature < beer.getMin()) {
                badBeers.add(beer);
            } else {
                goodBeers.add(beer);
            }
        }

        if (this.beers.containsAll(goodBeers)) {
            this.status = StatusType.OK;
        } else if (this.beers.containsAll(badBeers)) {
            this.status = StatusType.DANGER;
        } else {
            this.status = StatusType.WARNING;
        }
    }
}