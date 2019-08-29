package com.mib.rebalancer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true, value = { "price"})
public class Holding {
    public long fundId;
    public int units;
    public Balance balance;
    public Price price;

    public Holding() {

    }

    public Holding(long fundId, int units, Balance balance) {
        this.fundId = fundId;
        this.units = units;
        this.balance = balance;
    }

    public long getFundId() {
        return fundId;
    }

    public int getUnits() {
        return units;
    }

    public Balance getBalance() {
        return balance;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Holding{" + "fundId=" + fundId + ", units=" + units + ", balance=" + balance + "}";
    }
}
