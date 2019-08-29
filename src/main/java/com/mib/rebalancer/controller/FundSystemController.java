package com.mib.rebalancer.controller;

import com.mib.rebalancer.model.Fund;
import com.mib.rebalancer.model.Portfolio;
import com.mib.rebalancer.model.TransactionRequest;
import com.mib.rebalancer.model.TransactionResponse;
import com.mib.rebalancer.services.FundSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Exposes and proxies request to provided mock system endpoint
 */

@Controller
public class FundSystemController {
    private FundSystem fundSystem;

    @Autowired
    FundSystemController(FundSystem fundSystem) {
        this.fundSystem = fundSystem;
    }

    @GetMapping("/portfolios")
    @ResponseBody
    public List<Portfolio> getPortfolios(@RequestHeader("x-custid") String customerId) {

        return fundSystem.getPortfolios(customerId);
    }

    @GetMapping("/funds")
    @ResponseBody
    public List<Fund> getFunds() {

        return fundSystem.getFunds();
    }

    @PostMapping("/transaction")
    @ResponseBody
    public TransactionResponse getPortfolios(@RequestHeader("x-custid") String customerId, @RequestBody TransactionRequest transactionRequest) {

        return fundSystem.postTransaction(customerId, transactionRequest);
    }
}
