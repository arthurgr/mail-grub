package com.mailgrub.dto;

import java.math.BigDecimal;
import java.util.List;

public class RecipeResponse {
    private Integer id;
    private String name;
    private List<IngredientEntry> ingredients;
    private BigDecimal totalCost;

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

    public List<IngredientEntry> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientEntry> ingredients) {
        this.ingredients = ingredients;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public static class IngredientEntry {
        private Integer id;
        private String name;
        private String measurementType;
        private Double purchaseSize;
        private Double averageCost;
        private Double amount;

        public IngredientEntry() {}

        public IngredientEntry(Integer id, String name, String measurementType,
                               Double purchaseSize, Double averageCost, Double amount) {
            this.id = id;
            this.name = name;
            this.measurementType = measurementType;
            this.purchaseSize = purchaseSize;
            this.averageCost = averageCost;
            this.amount = amount;
        }

        // Getters and Setters...
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getMeasurementType() { return measurementType; }
        public void setMeasurementType(String measurementType) { this.measurementType = measurementType; }
        public Double getPurchaseSize() { return purchaseSize; }
        public void setPurchaseSize(Double purchaseSize) { this.purchaseSize = purchaseSize; }
        public Double getAverageCost() { return averageCost; }
        public void setAverageCost(Double averageCost) { this.averageCost = averageCost; }
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
    }
}
