
package com.mib.rebalancer.model;

import java.util.Objects;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
  private String action;
  private long fundId;
  private int units;

  public Transaction() {
  }

  public Transaction(String action, long fundId, int units) {
    this.fundId = fundId;
    this.action = action;
    this.units = units;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public long getFundId() {
    return fundId;
  }

  public void setFundId(long fundId) {
    this.fundId = fundId;
  }

  public int getUnits() {
    return units;
  }

  public void setUnits(int units) {
    this.units = units;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Transaction that = (Transaction) o;
    return fundId == that.fundId &&
            units == that.units &&
            Objects.equals(action, that.action);
  }

  @Override
  public int hashCode() {

    return Objects.hash(action, fundId, units);
  }

  @Override
  public String toString() {
    return "Transaction{" + "fundId=" + fundId + ", action=" + action + ", units=" + units + '}' + '\n';
  }
}
