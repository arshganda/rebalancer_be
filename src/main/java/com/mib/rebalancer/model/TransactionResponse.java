package com.mib.rebalancer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)

public class TransactionResponse {
    private int status;
    private long portfolioId;
    private long transactionId;
    private List<ExecutedTransaction> transactions;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public List<ExecutedTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<ExecutedTransaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionResponse that = (TransactionResponse) o;
        return status == that.status &&
                portfolioId == that.portfolioId &&
                transactionId == that.transactionId &&
                Objects.equals(transactions, that.transactions);
    }

    @Override
    public int hashCode() {

        return Objects.hash(status, portfolioId, transactionId, transactions);
    }
}
