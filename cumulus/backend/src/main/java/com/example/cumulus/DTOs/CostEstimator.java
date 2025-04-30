package com.example.cumulus.DTOs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CostEstimator {
    private double costPerMonth;
    private List<HashMap<?, ?>> costBreakdown;
    private Map<String, Double> priceSavings;


    public double getCostPerMonth() {
        return costPerMonth;
    }

    public void setCostPerMonth(double costPerMonth) {
        this.costPerMonth = costPerMonth;
    }

    public List<HashMap<?, ?>> getCostBreakdown() {
        return costBreakdown;
    }

    public void setCostBreakdown(List<HashMap<?, ?>> costBreakdown) {
        this.costBreakdown = costBreakdown;
    }

    public Map<String, Double> getPriceSavings() {
        return priceSavings;
    }

    public void setPriceSavings(Map<String, Double> priceSavings) {
        this.priceSavings = priceSavings;
    }
}
