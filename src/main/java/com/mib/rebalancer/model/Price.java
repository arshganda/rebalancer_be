package com.mib.rebalancer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Price extends CurrencyAmountBase {
    public Price() {}
    public Price(double a, String c) {
        super(a, c);
    }

    @Override
    public String toString() {
        return "Price{" + "amount=" + amount + ", currency='" + currency + '\'' + '}';
    }
}
