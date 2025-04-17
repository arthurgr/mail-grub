package com.mailgrub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String measurementType;

    private Double purchaseSize;

    private BigDecimal averageCost;

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

    public BigDecimal getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost;
    }

    // Calculated field: costPerOunce
    public Double getCostPerOunce() {
        if (purchaseSize == null || purchaseSize == 0 || averageCost == null) {
            return 0.0;
        }
        return averageCost
                .divide(BigDecimal.valueOf(purchaseSize), 4, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
