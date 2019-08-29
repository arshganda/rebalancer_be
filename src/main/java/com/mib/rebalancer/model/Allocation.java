
package com.mib.rebalancer.model;

import java.util.Objects;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class Allocation {
    private long fundId;
    private double percentage;

    public Allocation() {
    }

    public Allocation(long fundId, double percentage) {
        this.fundId = fundId;
        this.percentage = percentage;
    }

    public long getFundId() {
        return fundId;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Allocation that = (Allocation) o;
        return fundId == that.fundId && Double.compare(that.percentage, percentage) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fundId, percentage);
    }

    @Override
    public String toString() {
        return "Allocation{" + "fundId=" + fundId + ", percentage=" + percentage + '}';
    }
}
