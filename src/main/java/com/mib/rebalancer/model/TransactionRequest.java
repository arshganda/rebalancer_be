package com.mib.rebalancer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {
    private long portfolioId;
    private List<Transaction> instructions;

    public long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public List<Transaction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Transaction> instructions) {
        this.instructions = instructions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionRequest that = (TransactionRequest) o;
        return portfolioId == that.portfolioId &&
                Objects.equals(instructions, that.instructions);
    }

    @Override
    public int hashCode() {

        return Objects.hash(portfolioId, instructions);
    }
}

