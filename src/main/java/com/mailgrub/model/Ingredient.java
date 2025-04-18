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

    private MeasurementType measurementType;

    private Double purchaseSize;

    private BigDecimal averageCost;

    // Getter and setter methods
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

    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }

    public Double getPurchaseSize() {
        return purchaseSize;
    }

    public void setPurchaseSize(Double purchaseSize) {
        this.purchaseSize = purchaseSize;
    }

    public BigDecimal getAverageCost() {
        return averageCost.setScale(2, RoundingMode.HALF_UP);  // Ensure two decimal places for currency
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost.setScale(2, RoundingMode.HALF_UP);  // Ensure two decimal places for currency
    }

    // Method to calculate cost per ounce with conversion logic, returning BigDecimal
    public BigDecimal getCostPerOunce() {
        if (purchaseSize == null || purchaseSize == 0 || averageCost == null) {
            return BigDecimal.ZERO;
        }

        // Convert all units to ounces for calculation
        BigDecimal purchaseSizeInOunces = BigDecimal.valueOf(purchaseSize);
        if (measurementType == MeasurementType.KG) {
            purchaseSizeInOunces = purchaseSizeInOunces.multiply(BigDecimal.valueOf(35.274));  // 1 kg = 35.274 oz
        } else if (measurementType == MeasurementType.LB) {
            purchaseSizeInOunces = purchaseSizeInOunces.multiply(BigDecimal.valueOf(16));  // 1 lb = 16 oz
        }

        return averageCost
                .divide(purchaseSizeInOunces, 4, RoundingMode.HALF_UP) // Return cost per ounce as BigDecimal
                .setScale(2, RoundingMode.HALF_UP); // Round to 2 decimal places for money handling
    }
}
