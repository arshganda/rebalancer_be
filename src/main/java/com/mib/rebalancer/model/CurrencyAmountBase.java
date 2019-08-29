package com.mib.rebalancer.model;

public class CurrencyAmountBase {
    double amount;
    String currency;

    public CurrencyAmountBase() {
    }

    public CurrencyAmountBase(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
