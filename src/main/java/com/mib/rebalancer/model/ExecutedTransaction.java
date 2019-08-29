package com.mib.rebalancer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExecutedTransaction extends Transaction {
    private Price price;

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExecutedTransaction that = (ExecutedTransaction) o;
        return Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), price);
    }
}
