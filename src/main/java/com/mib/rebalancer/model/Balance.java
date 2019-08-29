package com.mib.rebalancer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance extends CurrencyAmountBase {

    public Balance() {

    }

    public Balance(double amount, String currency) {
        super(amount, currency);
    }

    @Override
    public String toString() {
        return "Balance{" + "amount=" + amount + ", currency='" + currency + '\'' + '}';
    }
}
