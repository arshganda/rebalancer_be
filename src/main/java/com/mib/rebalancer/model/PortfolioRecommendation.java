package com.mib.rebalancer.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true, value = { "portfolioIdentity" })
public class PortfolioRecommendation {

  @NotNull
  @GeneratedValue
  private PortfolioIdentity portfolioIdentity;

  @Id
  @GeneratedValue
  private long recommendationId;

  @Column
  @ElementCollection(fetch = FetchType.EAGER)
  private List<Transaction> transactions;

  public PortfolioRecommendation() {

  }

  public PortfolioRecommendation(List<Transaction> transactions) {
    this.transactions = transactions;
  }

  /**
   * @return the transactions
   */
  public List<Transaction> getTransactions() {
    return transactions;
  }

  /**
   * @param transactions the transactions to set
   */
  public void setTransactions(List<Transaction> transactions) {
    this.transactions = transactions;
  }

  /**
   * @return the portfolioRecommendationIdentity
   */
  public PortfolioIdentity getPortfolioIdentity() {
    return portfolioIdentity;
  }

  /**
   * @param portfolioRecommendationIdentity the portfolioRecommendationIdentity to
   *                                        set
   */
  public void setPortfolioIdentity(PortfolioIdentity portfolioIdentity) {
    this.portfolioIdentity = portfolioIdentity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PortfolioRecommendation that = (PortfolioRecommendation) o;
    return recommendationId == that.recommendationId;
  }

  @Override
  public int hashCode() {

    return Objects.hash(portfolioIdentity, recommendationId, transactions);
  }

  @Override
  public String toString() {
    return "PortfolioRecommendation{" +
            "portfolioIdentity=" + portfolioIdentity +
            ", recommendationId=" + recommendationId +
            ", transactions=" + transactions +
            '}';
  }

  /**
   * @return the recommendationId
   */
  public long getRecommendationId() {
    return recommendationId;
  }

  /**
   * @param recommendationId the recommendationId to set
   */
  public void setRecommendationId(long recommendationId) {
    this.recommendationId = recommendationId;
  }
}
