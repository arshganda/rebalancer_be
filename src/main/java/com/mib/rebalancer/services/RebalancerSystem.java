package com.mib.rebalancer.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mib.algorithm.model.MVPRebalance;
import com.mib.rebalancer.model.*;
import com.mib.rebalancer.repository.PortfolioRecommendationRepository;
import com.mib.rebalancer.repository.PortfolioSettingsRepository;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Configurable(autowire = Autowire.BY_TYPE)
public class RebalancerSystem {
    private PortfolioSettingsRepository portfolioSettingsRepository;
    private PortfolioRecommendationRepository portfolioRecommendationRepository;

    @Autowired
    public RebalancerSystem(PortfolioSettingsRepository portfolioSettingsRepository,
            PortfolioRecommendationRepository portfolioRecommendationRepository) {
        this.portfolioSettingsRepository = portfolioSettingsRepository;
        this.portfolioRecommendationRepository = portfolioRecommendationRepository;
    }

    @Transactional
    public PortfolioSettings setPortfolioSettings(String custId, Portfolio portfolio,
            PortfolioSettings portfolioSettings) {
        deletePortfolioRecommendations(custId, portfolio);

        portfolioSettings.setPortfolioIdentity(new PortfolioIdentity(custId, portfolio.getId()));
        portfolioSettingsRepository.save(portfolioSettings);
        return portfolioSettings;
    }

    public PortfolioSettings getPortfolioSettings(String custId, Portfolio portfolio) {
        return portfolioSettingsRepository.findById(new PortfolioIdentity(custId, portfolio.getId())).orElse(null);
    }

    public PortfolioRecommendation getPortfolioRecommendations(String custId, Portfolio portfolio) {
        List<PortfolioRecommendation> portfolioRecommendations = portfolioRecommendationRepository
                .findByPortfolioIdentity(new PortfolioIdentity(custId, portfolio.getId()));
        return portfolioRecommendations.isEmpty() ? null : portfolioRecommendations.get(0);
    }

    @Transactional
    public void deletePortfolioRecommendations(String custId, Portfolio portfolio) {
        PortfolioIdentity pID = new PortfolioIdentity(custId, portfolio.getId());
        portfolioRecommendationRepository.deleteByPortfolioIdentity(pID);
    }

    @Transactional
    public PortfolioRecommendation generatePortfolioRecommendations(String custId, Portfolio portfolio, PortfolioSettings portfolioSettings, List<Fund> funds) {
        // Remove all existing recommendations.
        PortfolioIdentity pID = new PortfolioIdentity(custId, portfolio.getId());
        portfolioRecommendationRepository.deleteByPortfolioIdentity(pID);

        // TODO:
        // REPLACE WITH CALL TO GENERATE RECOMMENDATIONS
        // REPLACE CODE HERE START
        List<PortfolioRecommendation> recommendations = new ArrayList<>();

        PortfolioRecommendation portfolioRecommendation = MVPRebalance.rebalance(portfolio, portfolioSettings, funds);
        portfolioRecommendationRepository.save(portfolioRecommendation);
        // REPLACE CODE HERE END

        return portfolioRecommendation;
    }

    public PortfolioRecommendation modifyPortfolioRecommendation(String custId, Portfolio portfolio, long recommendationId,
            List<Transaction> transactions) {

        PortfolioRecommendation portfolioRecommendation = portfolioRecommendationRepository.findById(recommendationId)
                .orElse(null);

        if (portfolioRecommendation != null) {
            List<Transaction> pTransactions = portfolioRecommendation.getTransactions();
            for (Transaction t : transactions) {
                Transaction pTransaction = pTransactions.stream().filter(pt -> pt.getFundId() == t.getFundId())
                        .findFirst().orElse(null);

                if (pTransaction != null) {
                    int deltaUnits = t.getUnits();

                    if (!pTransaction.getAction().equals(t.getAction())) {
                        deltaUnits = -deltaUnits;
                    }

                    int newUnits = pTransaction.getUnits() + deltaUnits;

                    if (newUnits < 0) {
                        pTransaction.setAction(getInverseAction(pTransaction.getAction()));
                        pTransaction.setUnits(-newUnits);
                    } else if (newUnits == 0) {
                        pTransactions.remove(pTransaction);
                    } else {
                        pTransaction.setUnits(newUnits);
                    }
                } else {
                    if (t.getUnits() < 0) {
                        t.setAction(getInverseAction(t.getAction()));
                        t.setUnits(-t.getUnits());
                    }

                    pTransactions.add(t);
                }
            }
            // portfolioRecommendation.setTransactions(pTransactions);
            portfolioRecommendationRepository.save(portfolioRecommendation);
        }

        return portfolioRecommendation;
    }

    public String getInverseAction(String action) {
        return action.equals("buy") ? "sell" : "buy";
    }
}
