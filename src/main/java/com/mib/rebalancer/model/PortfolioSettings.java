package com.mib.rebalancer.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true, value = { "portfolioIdentity" })
public class PortfolioSettings {
    @EmbeddedId
    private PortfolioIdentity portfolioIdentity;

    private double deviation;
    private String type;
    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Allocation> allocations;

    public PortfolioSettings() {

    }

    public PortfolioSettings(double deviation, String type, List<Allocation> allocations) {
        this.deviation = deviation;
        this.type = type;
        this.allocations = allocations;
    }

    public double getDeviation() {
        return deviation;
    }

    public String getType() {
        return type;
    }

    public void setDeviation(double deviation) {
        this.deviation = deviation;
    }

    public List<Allocation> getAllocations() {
        return allocations;
    }

    public PortfolioIdentity getPortfolioIdentity() {
        return portfolioIdentity;
    }

    public void setAllocations(List<Allocation> allocations) {
        this.allocations = allocations;
    }

    public void setPortfolioIdentity(PortfolioIdentity portfolioIdentity) {
        this.portfolioIdentity = portfolioIdentity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PortfolioSettings that = (PortfolioSettings) o;
        return deviation == that.deviation && Objects.equals(type, that.type)
                && Objects.equals(allocations, that.allocations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviation, type, allocations);
    }

    @Override
    public String toString() {
        return "PortfolioSettings{" + "portfolioIdentity=" + portfolioIdentity + ", deviation=" + deviation + ", type='"
                + type + '\'' + ", allocations=" + allocations + '}';
    }
}
