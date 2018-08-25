package com.restApi.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.restApi.spring.enums.BeerType;
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

    @OneToMany(mappedBy = "beer", fetch = FetchType.EAGER)
    private List<BeerContainers> beerContainers = new ArrayList<>();

    public Beer() {

    }

    public Beer(String description, BeerType type, Double min, Double max) {
        super();

        this.description = description;
        this.type = type;
        this.min = min;
        this.max = max;
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

    public List<BeerContainers> getBeerContainers() {
        return beerContainers;
    }
}