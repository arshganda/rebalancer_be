package com.mib.rebalancer.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Portfolio {
    private long id;
    private List<Holding> holdings;

    public Portfolio(long id, List<Holding> h) {
        this.id = id;
        this.holdings = h;
    }

    public long getId() {
        return id;
    }

    public Portfolio() {

    }

    public Portfolio(long id) {
        this.id = id;
    }

    public List<Holding> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<Holding> holdings) {
        this.holdings = holdings;
    }

    @Override
    public String toString() {
        return "Portfolio{" + "id=" + id + ", holdings=" + holdings + '}';
    }
}
