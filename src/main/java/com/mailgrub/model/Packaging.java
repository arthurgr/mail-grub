package com.mailgrub.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class Packaging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String packagingMaterials;

    private BigDecimal averageCost;

    private Integer quantity;

    private BigDecimal costPerUnit;

    private String procurement;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPackagingMaterials() {
        return packagingMaterials;
    }

    public void setPackagingMaterials(String packagingMaterials) {
        this.packagingMaterials = packagingMaterials;
    }

    public BigDecimal getAverageCost() {
        return averageCost.setScale(2, RoundingMode.HALF_UP);
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost.setScale(2, RoundingMode.HALF_UP);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCostPerUnit() {
        return costPerUnit.setScale(2, RoundingMode.HALF_UP);
    }

    public void setCostPerUnit(BigDecimal costPerUnit) {
        this.costPerUnit = costPerUnit.setScale(2, RoundingMode.HALF_UP);
    }

    public String getProcurement() {
        return procurement;
    }

    public void setProcurement(String procurement) {
        this.procurement = procurement;
    }
}
