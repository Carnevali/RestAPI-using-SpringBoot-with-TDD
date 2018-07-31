package com.restApi.spring.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.restApi.spring.enums.BeerType;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;

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
    private BigDecimal min;

    @Column(name="max", nullable=false)
    private BigDecimal max;

    @ManyToOne
    @JoinColumn(name="containers_id")
    @JsonBackReference
    private Containers containers;

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

    public BeerType getType() {
        return type;
    }

    public void setType(BeerType type) {
        this.type = type;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public Containers getContainers() {
        return containers;
    }

    public void setContainers(Containers containers) {
        this.containers = containers;
    }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }
}