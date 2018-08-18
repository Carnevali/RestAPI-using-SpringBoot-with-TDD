package com.restApi.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.restApi.spring.enums.BeerType;
import com.restApi.spring.enums.StatusType;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

@Entity
@Table(name="beer")
@JsonIgnoreProperties( ignoreUnknown = true )
public class Beer implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name="description", nullable=false)
    private String description;

    @Column(name="type", nullable=false)
    @Enumerated(EnumType.STRING)
    private BeerType type;

    @Column(name="min", nullable=false)
    private Double min;

    @Column(name="max", nullable=false)
    private Double max;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(name = "beer_container", joinColumns = { @JoinColumn(name = "beer_id") }, inverseJoinColumns = { @JoinColumn(name = "container_id") })
    private List<Containers> containers = new ArrayList<>();

    @Column(name="status", nullable=false)
    private StatusType status = StatusType.OK;


    public Beer() {

    }

    public Beer(String description, BeerType type, Double min, Double max, List<Containers> containers, StatusType status) {
        super();

        this.description = description;
        this.type = type;
        this.min = min;
        this.max = max;
        this.containers = containers;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BeerType getType() {
        return type;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public List<Containers> getContainers() {
        return containers;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(Containers container) {
        if (container.getTemperature() > this.max) {
            this.status = StatusType.WARNING;
        } else if (container.getTemperature() < this.min) {
            this.status = StatusType.WARNING;
        } else {
            this.status = StatusType.OK;
        }
    }
}