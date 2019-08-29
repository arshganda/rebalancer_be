package com.mib.rebalancer.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Deviation {
    private double deviation;

    public double getDeviation() {
        return deviation;
    }

    public void setDeviation(double deviation) {
        this.deviation = deviation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Deviation deviation1 = (Deviation) o;
        return Double.compare(deviation1.deviation, deviation) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(deviation);
    }
}
