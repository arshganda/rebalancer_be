package com.mib.rebalancer.repository;

import java.util.List;

import com.mib.rebalancer.model.PortfolioIdentity;
import com.mib.rebalancer.model.PortfolioRecommendation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRecommendationRepository extends JpaRepository<PortfolioRecommendation, Long> {
  public List<PortfolioRecommendation> findByPortfolioIdentity(PortfolioIdentity portfolioIdentity);

  public void deleteByPortfolioIdentity(PortfolioIdentity portfolioIdentity);
}
