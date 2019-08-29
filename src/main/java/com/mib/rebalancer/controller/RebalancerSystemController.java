package com.mib.rebalancer.controller;

import java.util.List;

import com.mib.algorithm.model.MVPRebalance;
import com.mib.rebalancer.model.Allocation;
import com.mib.rebalancer.model.Deviation;
import com.mib.rebalancer.model.Portfolio;
import com.mib.rebalancer.model.PortfolioRecommendation;
import com.mib.rebalancer.model.PortfolioSettings;
import com.mib.rebalancer.model.Transaction;
import com.mib.rebalancer.model.TransactionRequest;
import com.mib.rebalancer.model.TransactionResponse;
import com.mib.rebalancer.services.FundSystem;
import com.mib.rebalancer.services.RebalancerSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class RebalancerSystemController {
    private RebalancerSystem rebalancerSystem;
    private FundSystem fundSystem;

    @Autowired
    public RebalancerSystemController(RebalancerSystem rebalancerSystem, FundSystem fundSystem) {
        this.rebalancerSystem = rebalancerSystem;
        this.fundSystem = fundSystem;
    }

    @PostMapping("/portfolio/{id}")
    @ResponseBody
    public PortfolioSettings postPortfolioSettings(@RequestHeader("x-custid") String customerId, @PathVariable long id,
            @RequestBody PortfolioSettings portfolioSettings) {
        // Validate portfolio exists.
        List<Portfolio> portfolios = fundSystem.getPortfolios(customerId);
        double totalAllocation = 0;

        Portfolio requestedPortfolio = portfolios.stream().filter(p -> p.getId() == id).findFirst().orElse(null);

        if (requestedPortfolio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio with id " + id + " not found");
        }
        // CHECK NEGATIVE DEVIATION
        if (portfolioSettings.getDeviation() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "deviation cannot be negative");
        }

        // TODO: Check there are allocations for every holding

        // portfolioSettings.getAllocations();
        for (Allocation port : portfolioSettings.getAllocations()) {
            totalAllocation += port.getPercentage();
        }

        if (Math.round(totalAllocation) != 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Allocation percentages do not add up to 100%");
        }

        return rebalancerSystem.setPortfolioSettings(customerId, requestedPortfolio, portfolioSettings);
    }

    @GetMapping("/portfolio/{id}")
    @ResponseBody
    public PortfolioSettings getPortfolioSettings(@RequestHeader("x-custid") String customerId, @PathVariable long id) {

        List<Portfolio> portfolios = fundSystem.getPortfolios(customerId);
        Portfolio requestedPortfolio = portfolios.stream().filter(p -> p.getId() == id).findFirst().orElse(null);

        if (requestedPortfolio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio with id " + id + " not found");
        }

        try {
            return rebalancerSystem.getPortfolioSettings(customerId, requestedPortfolio);
        } catch (Exception e) {
            return null;
        }
    }

    @PutMapping("/portfolio/{id}/allocations")
    @ResponseBody
    public List<Allocation> putAllocations(@RequestHeader("x-custid") String customerId, @PathVariable long id,
            @RequestBody List<Allocation> allocations) {
        List<Portfolio> portfolios = fundSystem.getPortfolios(customerId);
        Portfolio requestedPortfolio = portfolios.stream().filter(p -> p.getId() == id).findFirst().orElse(null);

        if (requestedPortfolio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio with id " + id + " not found");
        }

        PortfolioSettings portfolioSettings = rebalancerSystem.getPortfolioSettings(customerId, requestedPortfolio);

        double totalAllocation = 0;
        for (Allocation port : allocations) {
            totalAllocation += port.getPercentage();
        }

        if (Math.round(totalAllocation) != 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Allocation percentages do not add up to 100%");
        }

        if (portfolioSettings != null) {
            portfolioSettings.setAllocations(allocations);
            rebalancerSystem.setPortfolioSettings(customerId, requestedPortfolio, portfolioSettings);
            return allocations;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PortfolioSetting with id " + id + " not found");
        }
    }

    @PutMapping("/portfolio/{id}/deviation")
    @ResponseBody
    public Deviation putDeviation(@RequestHeader("x-custid") String customerId, @PathVariable long id,
            @RequestBody Deviation deviation) {

        List<Portfolio> portfolios = fundSystem.getPortfolios(customerId);
        Portfolio requestedPortfolio = portfolios.stream().filter(p -> p.getId() == id).findFirst().orElse(null);

        if (requestedPortfolio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio with id " + id + " not found");
        }

        PortfolioSettings portfolioSettings = rebalancerSystem.getPortfolioSettings(customerId, requestedPortfolio);
        if (deviation.getDeviation() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "deviation cannot be negative");
        }

        if (portfolioSettings != null) {
            portfolioSettings.setDeviation(deviation.getDeviation());
            rebalancerSystem.setPortfolioSettings(customerId, requestedPortfolio, portfolioSettings);
            return deviation;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PortfolioSetting with id " + id + " not found");
        }

    }

    @GetMapping("/portfolio/{id}/recommendations/available")
    @ResponseBody
    public Boolean getRecommendationsAvailability(@RequestHeader("x-custid") String customerId, @PathVariable long id) {
        List<Portfolio> portfolios = fundSystem.getPortfolios(customerId);
        Portfolio requestedPortfolio = portfolios.stream().filter(p -> p.getId() == id).findFirst().orElse(null);

        if (requestedPortfolio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio with id " + id + " not found");
        }

        PortfolioSettings portfolioSettings = rebalancerSystem.getPortfolioSettings(customerId, requestedPortfolio);

        return MVPRebalance.hasRecommendations(requestedPortfolio, portfolioSettings, fundSystem.getFunds());
    }

    @GetMapping("/portfolio/{id}/rebalance")
    @ResponseBody
    public PortfolioRecommendation getRecommendations(@RequestHeader("x-custid") String customerId,
            @PathVariable long id) {
        List<Portfolio> portfolios = fundSystem.getPortfolios(customerId);
        Portfolio requestedPortfolio = portfolios.stream().filter(p -> p.getId() == id).findFirst().orElse(null);

        // Validate portfolio exists.
        if (requestedPortfolio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio with id " + id + " not found");
        }

        return rebalancerSystem.getPortfolioRecommendations(customerId, requestedPortfolio);
    }

    @PostMapping("/portfolio/{id}/rebalance")
    @ResponseBody
    public PortfolioRecommendation generateRecommendations(@RequestHeader("x-custid") String customerId,
            @PathVariable long id) {
        List<Portfolio> portfolios = fundSystem.getPortfolios(customerId);
        Portfolio requestedPortfolio = portfolios.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        // Validate portfolio exists.
        if (requestedPortfolio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio with id " + id + " not found");
        }

        PortfolioSettings portfolioSettings = rebalancerSystem.getPortfolioSettings(customerId, requestedPortfolio);
        return rebalancerSystem.generatePortfolioRecommendations(customerId, requestedPortfolio, portfolioSettings,
                fundSystem.getFunds());
    }

    @PostMapping(value = "/portfolio/{id}/recommendation/{recommendationId}/execute")
    @ResponseBody
    public boolean executePortfolioRecommendation(@RequestHeader("x-custid") String customerId, @PathVariable long id,
            @PathVariable long recommendationId) {
        List<Portfolio> portfolios = fundSystem.getPortfolios(customerId);
        Portfolio requestedPortfolio = portfolios.stream().filter(p -> p.getId() == id).findFirst().orElse(null);

        // Validate portfolio exists.
        if (requestedPortfolio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio with id " + id + " not found");
        }

        PortfolioRecommendation portfolioRecommendation = rebalancerSystem.getPortfolioRecommendations(customerId,
                requestedPortfolio);

        if (portfolioRecommendation != null) {
            TransactionRequest request = new TransactionRequest();
            request.setPortfolioId(id);
            request.setInstructions(portfolioRecommendation.getTransactions());
            TransactionResponse response = fundSystem.postTransaction(customerId, request);
            rebalancerSystem.deletePortfolioRecommendations(customerId, requestedPortfolio);

            if (response == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction failed");
            }

            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Portfolio recommendation with id " + recommendationId + " not found");
        }
    }

    @PutMapping(value = "/portfolio/{id}/recommendation/{recommendationId}/modify")
    @ResponseBody
    public PortfolioRecommendation modifyPortfolioRecommendation(@RequestHeader("x-custid") String customerId,
            @PathVariable long id, @PathVariable long recommendationId, @RequestBody List<Transaction> transactions) {
        List<Portfolio> portfolios = fundSystem.getPortfolios(customerId);
        Portfolio requestedPortfolio = portfolios.stream().filter(p -> p.getId() == id).findFirst().orElse(null);

        // Validate portfolio exists.
        if (requestedPortfolio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio with id " + id + " not found");
        }

        return rebalancerSystem.modifyPortfolioRecommendation(customerId, requestedPortfolio, recommendationId,
                transactions);
    }

}
