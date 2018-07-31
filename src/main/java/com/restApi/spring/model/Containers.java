package com.restApi.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

@Entity
@Table(name="containers")
@JsonIgnoreProperties( ignoreUnknown = true )
public class Containers implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name="description", nullable=false)
    private String description;

    @Column(name="temperature", nullable=false)
    private BigDecimal temperature;

    @OneToMany(mappedBy = "containers", cascade = CascadeType.ALL)
    private List<Beer> beers = new ArrayList<>();

    @Column(name="status", nullable=false)
    private Integer status = 0;

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public List<Beer> getBeers() {
        return beers;
    }

    public void setBeers(List<Beer> beers) {
        this.beers = beers;
    }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }
}