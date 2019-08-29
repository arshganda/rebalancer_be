package com.mib.rebalancer;

import com.mib.rebalancer.model.Fund;
import com.mib.rebalancer.model.Holding;
import com.mib.rebalancer.model.Portfolio;
import com.mib.rebalancer.services.FundSystem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FundSystemTest {
    private static final Logger log = LoggerFactory.getLogger(RebalancerApplication.class);
    @Autowired
    FundSystem fundSystem;

    @Test
    public void testGetFund() {
        Fund fund = fundSystem.getFund(23503);
        assertEquals(fund.getFundId(), 23503);

        log.info(fund.toString());
    }

    @Test
    public void testGetPortfolio() {
        List<Portfolio> portfolios = fundSystem.getPortfolios("i42bumx1oe");
        assertEquals(2, portfolios.size());

        log.info(portfolios.toString());
    }

    @Test
    public void testGetFunds() {
        List<Fund> funds = fundSystem.getFunds();
        assertEquals(8, funds.size());

        log.info(funds.toString());
    }

    @Test
    public void testPortfolio(){
        List<Holding> list = new ArrayList<>();
        Holding h = new Holding();
        list.add(h);
        Portfolio p = new Portfolio(1234,list);
        assertEquals(1,p.getHoldings().size());
    }

}
