package com.mib.rebalancer.repository;

import com.mib.rebalancer.model.PortfolioIdentity;
import com.mib.rebalancer.model.PortfolioSettings;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioSettingsRepository extends JpaRepository<PortfolioSettings, PortfolioIdentity> {
}
