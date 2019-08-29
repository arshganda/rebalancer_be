package com.mib.rebalancer;

import com.mib.rebalancer.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ModelTest {

    @Test
    public void allocationTest(){
        Allocation a1 = new Allocation(1,100);
        Allocation a2 = new Allocation(2,70);
        Assert.assertTrue(!a1.equals(a2));
        Assert.assertTrue(a1.equals(a1));
        Assert.assertTrue(!a1.equals(null));
        Assert.assertTrue(a1.hashCode() != a2.hashCode());
    }

    @Test
    public void deviationTest(){
        Deviation d1 = new Deviation();
        d1.setDeviation(1.0);
        Deviation d2 = new Deviation();
        d2.setDeviation(2.0);
        Deviation d3 = new Deviation();
        d1.setDeviation(1.0);

        Assert.assertTrue(d1.hashCode() != d2.hashCode());
        Assert.assertTrue(d1.hashCode() == d1.hashCode());
        Assert.assertTrue(d1.equals(d1));
        Assert.assertTrue(!d1.equals(d2));
        Assert.assertTrue(!d1.equals(null));

    }

    @Test
    public void avgUnitTest(){
        AvgUnitPrice a1 = new AvgUnitPrice(20,"CAD");
        AvgUnitPrice a2 = new AvgUnitPrice(20,"USD");
        Assert.assertTrue(a1.toString()!=a2.toString());

    }

    @Test
    public void executeTransactionTest(){
        ExecutedTransaction e1 = new ExecutedTransaction();
        e1.setPrice(new Price(10,"CAD"));
        Assert.assertEquals("CAD", e1.getPrice().getCurrency());
        ExecutedTransaction e2 = new ExecutedTransaction();
        e1.setPrice(new Price(10,"CAD"));
        Assert.assertTrue(e1.hashCode()!=e2.hashCode());
        Assert.assertTrue(!e1.equals(e2));

    }

    @Test
    public void portfolioIdentityTest(){
        PortfolioIdentity p1 = new PortfolioIdentity();
        p1.setCustomerId("123");
        Assert.assertEquals("123",p1.getCustomerId());
        p1.setPortfolioId(123);
        Assert.assertEquals(123,p1.getPortfolioId());
        PortfolioIdentity p2 = new PortfolioIdentity();
        p1.setPortfolioId(111);
        p1.setCustomerId("111");
        Assert.assertTrue(!p1.equals(p2));
        Assert.assertTrue(p1.hashCode()!=p2.hashCode());
        Assert.assertTrue(p1.toString() != p2.toString());
    }

    @Test
    public void transactionRequestTest(){
        TransactionRequest t1 = new TransactionRequest();
        t1.setPortfolioId(1);
        Assert.assertEquals(1, t1.getPortfolioId());
        Assert.assertEquals(null, t1.getInstructions());
        TransactionRequest t2 = new TransactionRequest();
        Assert.assertTrue(!t1.equals(t2));
        Assert.assertTrue(t1.hashCode() != t2.hashCode());

    }
}
