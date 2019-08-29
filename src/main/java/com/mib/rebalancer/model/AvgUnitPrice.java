package com.mib.rebalancer.model;

public class AvgUnitPrice extends CurrencyAmountBase {
    public AvgUnitPrice(double a, String c) {
        super(a, c);
    }

    @Override
    public String toString() {
        return "AvgUnitPrice{" + "amount=" + amount + ", currency='" + currency + '\'' + '}';
    }
}
