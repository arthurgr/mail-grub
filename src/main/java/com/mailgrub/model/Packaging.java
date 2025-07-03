package com.mailgrub.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class Packaging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String packagingMaterials;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal averageCost;

    private Integer quantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
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
        return averageCost == null
                ? BigDecimal.ZERO
                : averageCost.setScale(2, RoundingMode.HALF_UP);
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost == null
                ? BigDecimal.ZERO
                : averageCost.setScale(2, RoundingMode.HALF_UP);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProcurement() {
        return procurement;
    }

    public void setProcurement(String procurement) {
        this.procurement = procurement;
    }

    public BigDecimal getCostPerUnit() {
        return costPerUnit == null
                ? BigDecimal.ZERO
                : costPerUnit.setScale(2, RoundingMode.HALF_UP);
    }

    public void setCostPerUnit(BigDecimal costPerUnit) {
        this.costPerUnit = costPerUnit == null
                ? BigDecimal.ZERO
                : costPerUnit.setScale(2, RoundingMode.HALF_UP);
    }
}
