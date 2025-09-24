package com.mailgrub.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "ingredient")
public class Ingredient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "tenant_id", nullable = false, updatable = false)
  private String tenantId; // <--- add this field

  private String name;

  @Enumerated(EnumType.STRING)
  private MeasurementType measurementType;

  private Double purchaseSize;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
  private BigDecimal averageCost;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
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
    return averageCost == null ? null : averageCost.setScale(2, RoundingMode.HALF_UP);
  }

  public void setAverageCost(BigDecimal averageCost) {
    this.averageCost = averageCost == null ? null : averageCost.setScale(2, RoundingMode.HALF_UP);
  }

  @JsonProperty("costPerOunce")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
  public BigDecimal getCostPerOunce() {
    if (purchaseSize == null || purchaseSize == 0 || averageCost == null) {
      return BigDecimal.ZERO;
    }

    BigDecimal purchaseSizeInOunces = BigDecimal.valueOf(purchaseSize);
    if (measurementType == MeasurementType.KG) {
      purchaseSizeInOunces = purchaseSizeInOunces.multiply(BigDecimal.valueOf(35.274));
    } else if (measurementType == MeasurementType.LB) {
      purchaseSizeInOunces = purchaseSizeInOunces.multiply(BigDecimal.valueOf(16));
    }

    return averageCost
        .divide(purchaseSizeInOunces, 4, RoundingMode.HALF_UP)
        .setScale(2, RoundingMode.HALF_UP);
  }
}
