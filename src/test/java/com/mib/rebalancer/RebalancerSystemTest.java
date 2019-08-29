package com.mib.rebalancer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mib.rebalancer.model.*;
import com.mib.rebalancer.services.RebalancerSystem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableSpringConfigured
@ActiveProfiles("test")
public class RebalancerSystemTest {
    private static final Logger log = LoggerFactory.getLogger(RebalancerApplication.class);

    private Portfolio portfolio;
    private PortfolioSettings portfolioSettings;
    private List<Fund> funds;

    @Autowired
    RebalancerSystem rebalancerSystem;

    @Before
    public void testSetup() {
        funds = new ArrayList<>();

        Fund fund1234 = new Fund();
        fund1234.setFundId(1234);
        fund1234.setPrice(new Price(1, "CAD"));
        funds.add(fund1234);

        Fund fund1235 = new Fund();
        fund1235.setFundId(1234);
        fund1235.setPrice(new Price(1, "CAD"));
        funds.add(fund1234);

        portfolio = new Portfolio(1);
        portfolio.setHoldings(Arrays.asList(
                new Holding(1234, 5, new Balance(5, "CAD")),
                new Holding(1235, 10, new Balance(999, "CAD"))
        ));

        portfolioSettings = new PortfolioSettings(1, "fund", Arrays.asList(
                new Allocation(1234, 80),
                new Allocation(1235, 20)
        ));

        portfolioSettings.setPortfolioIdentity(new PortfolioIdentity("abc", 1));
    }

    @Test
    public void saveAndRetrievePortfolioSettings() {
        List<Allocation> allocationList = new ArrayList<>();
        allocationList.add(new Allocation(1, 4));

        PortfolioSettings portfolioSettings = new PortfolioSettings(2, "fund", allocationList);

        rebalancerSystem.setPortfolioSettings("abc", portfolio, portfolioSettings);

        PortfolioSettings foundPortfolioSettings = rebalancerSystem.getPortfolioSettings("abc", portfolio);

        assertNotNull(foundPortfolioSettings);
        assertEquals(portfolioSettings, foundPortfolioSettings);
    }

    @Test
    public void generateAndGetPortfolioRecommendations() {
        PortfolioRecommendation recommendation = rebalancerSystem.getPortfolioRecommendations("abc", portfolio);

        assertNull(recommendation);

        PortfolioRecommendation generatedRecommendation = rebalancerSystem.generatePortfolioRecommendations("abc", portfolio, portfolioSettings, funds);
        recommendation = rebalancerSystem.getPortfolioRecommendations("abc", portfolio);

        assertEquals(recommendation, generatedRecommendation);
    }

    @Test
    public void modifyPortfolioRecommendations() {
        PortfolioRecommendation recommendation = rebalancerSystem.generatePortfolioRecommendations("abc", portfolio, portfolioSettings, funds);

        if (recommendation == null){
            fail("No recommendations. Cannot proceed.");
        }

        Transaction originalTransaction = recommendation.getTransactions().get(0);
        Transaction modifyT = new Transaction(originalTransaction.getAction(), originalTransaction.getFundId(), 200);

        PortfolioRecommendation newRecommendation = rebalancerSystem.modifyPortfolioRecommendation("abc", portfolio, recommendation.getRecommendationId(), Arrays.asList(modifyT));
        assertEquals(originalTransaction.getUnits() + 200, newRecommendation.getTransactions().get(0).getUnits());
    }

}
