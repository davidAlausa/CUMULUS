package com.example.cumulus.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.Map;


public class CostEstimator {
    private double initialCost;
    private double costPerMonth;
    private List<List<String>> costBreakdown;
    private Map<String, Double> priceSavings;

    public double getInitialCost() {
        return initialCost;
    }

    public void setInitialCost(double initialCost) {
        this.initialCost = initialCost;
    }

    public double getCostPerMonth() {
        return costPerMonth;
    }

    public void setCostPerMonth(double costPerMonth) {
        this.costPerMonth = costPerMonth;
    }

    public List<List<String>> getCostBreakdown() {
        return costBreakdown;
    }

    public void setCostBreakdown(List<List<String>> costBreakdown) {
        this.costBreakdown = costBreakdown;
    }

    public Map<String, Double> getPriceSavings() {
        return priceSavings;
    }

    public void setPriceSavings(Map<String, Double> priceSavings) {
        this.priceSavings = priceSavings;
    }
}
