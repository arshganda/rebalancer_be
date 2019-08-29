package com.mib.rebalancer.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class PortfolioIdentity implements Serializable {
    @NotNull
    private String customerId;

    @NotNull
    private long portfolioId;

    public PortfolioIdentity() {

    }

    public PortfolioIdentity(@NotNull String customerId, @NotNull long portfolioId) {
        this.customerId = customerId;
        this.portfolioId = portfolioId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(long portfolioId) {
        this.portfolioId = portfolioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortfolioIdentity that = (PortfolioIdentity) o;
        return portfolioId == that.portfolioId &&
                Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(customerId, portfolioId);
    }

    @Override
    public String toString() {
        return "PortfolioSettings{" + "customerId= " + customerId + ", portfolioId= " + portfolioId + '}';
    }
}
