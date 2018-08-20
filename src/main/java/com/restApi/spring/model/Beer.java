package com.restApi.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.restApi.spring.enums.BeerType;
import com.restApi.spring.enums.StatusType;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "beers_containers",
            joinColumns = { @JoinColumn(name = "containers_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "beers_id", referencedColumnName = "id") })
    private Set<Containers> containers = new HashSet<>();

    @Column(name="status", nullable=false)
    private StatusType status = StatusType.OK;


    public Beer() {

    }

    public Beer(String description, BeerType type, Double min, Double max, Set<Containers> containers, StatusType status) {
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

    public Set<Containers> getContainers() {
        return containers;
    }

    public StatusType getStatus() {
        return status;
    }

    public void updateStatus(Double temperature) {
        if (temperature > this.max) {
            this.status = StatusType.WARNING;
        } else if (temperature < this.min) {
            this.status = StatusType.WARNING;
        } else {
            this.status = StatusType.OK;
        }
    }
}