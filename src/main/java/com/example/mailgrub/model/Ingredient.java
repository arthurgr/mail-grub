package com.example.mailgrub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String measurementType;

    private Double purchaseSize;

    private Double averageCost;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public Double getPurchaseSize() {
        return purchaseSize;
    }

    public void setPurchaseSize(Double purchaseSize) {
        this.purchaseSize = purchaseSize;
    }

    public Double getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(Double averageCost) {
        this.averageCost = averageCost;
    }

    // Calculated field: costPerOunce
    public Double getCostPerOunce() {
        if (purchaseSize == null || purchaseSize == 0) {
            return 0.0; // Avoid division by zero
        }
        return averageCost / purchaseSize;
    }
}
