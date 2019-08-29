package com.mib.algorithm.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mib.rebalancer.model.Allocation;
import com.mib.rebalancer.model.Balance;
import com.mib.rebalancer.model.Fund;
import com.mib.rebalancer.model.Holding;
import com.mib.rebalancer.model.Portfolio;
import com.mib.rebalancer.model.PortfolioIdentity;
import com.mib.rebalancer.model.PortfolioRecommendation;
import com.mib.rebalancer.model.PortfolioSettings;
import com.mib.rebalancer.model.Price;
import com.mib.rebalancer.model.Transaction;

public class MVPRebalance {
    /*
     * Method to perform basic rebalancing
     * 
     */
    /*
     * Perform Percentage-of-Portfolio (see below URL) Checks whether portfolio is
     * above deviation threshold
     *
     * https://www.investopedia.com/articles/stocks/11/rebalancing-strategies.asp
     *
     */
    public static PortfolioRecommendation rebalance(Portfolio p, PortfolioSettings ps, List<Fund> funds) {
        for (Holding h : p.getHoldings()) {
            Fund matchingFund = funds.stream().filter(f -> f.getFundId() == h.getFundId()).findFirst().orElse(null);
            if (matchingFund != null) {
                h.setPrice(matchingFund.getPrice());
            } else {
                h.setPrice(new Price(0, ""));
            }
        }

        // can we assume ps identity = p.id
        double deviation = ps.getDeviation() == 0 ? 0.1 : ps.getDeviation();
        double maxDifference = findMaxHoldingDifference(p, ps);

        // List<PortfolioRecommendation> to return
        List<PortfolioRecommendation> recommendations = new ArrayList<>();

        // nothing to do here because all fund's differences are less than deviation
        if (deviation > maxDifference) {
            // Return a recommendation that does nothing (no change to portfolio)
            return null;
        } else {
            return rebalanceWithoutAdding(p, ps);
        }
    }

    public static boolean hasRecommendations(Portfolio p, PortfolioSettings ps, List<Fund> funds) {
        for (Holding h : p.getHoldings()) {
            Fund matchingFund = funds.stream().filter(f -> f.getFundId() == h.getFundId()).findFirst().orElse(null);
            if (matchingFund != null) {
                h.setPrice(matchingFund.getPrice());
            } else {
                h.setPrice(new Price(0, ""));
            }
        }
        // can we assume ps identity = p.id
        double deviation = ps.getDeviation() == 0 ? 0.1 : ps.getDeviation();
        double maxDifference = findMaxHoldingDifference(p, ps);
        System.out.println(maxDifference);

        // nothing to do here because all fund's differences are less than deviation
        return (deviation <= maxDifference);
    }

    /*
     * Used in a case where there is no need to rebalance because the portfolio is
     * within deviation threshold
     */
    public static PortfolioRecommendation makeNoTransanction(Portfolio p, PortfolioSettings ps) {
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < p.getHoldings().size(); i++) {
            Transaction t = new Transaction("buy", ps.getAllocations().get(i).getFundId(), 0);
            transactions.add(t);
        }

        PortfolioRecommendation noAction = new PortfolioRecommendation(transactions);
        PortfolioIdentity pID = ps.getPortfolioIdentity();
        noAction.setPortfolioIdentity(pID);
        return noAction;
    }

    /*
     * TODO
     */

    public static PortfolioRecommendation rebalanceWithoutAdding(Portfolio p, PortfolioSettings ps) {
        // User holdings
        List<Holding> portfolioHoldings = p.getHoldings();
        // portfolioID
        PortfolioIdentity portfolioId = ps.getPortfolioIdentity();
        // List of values of each fund type
        List<Double> holdingValues = new ArrayList<Double>();
        // Wanted value of each of the fund type according to ps
        List<Double> wantedValue = new ArrayList<Double>();
        // Value to buy/sell - positive is to buy
        List<Double> valuesToBuyOrSell = new ArrayList<Double>();
        // Number of fund type to buy/sell
        List<Double> countToBuyOrSell = new ArrayList<Double>();
        // Placeholder for the rebalanced holdings
        List<Holding> rebalancedHoldings = new ArrayList<>();
        // Adjusted Holdings
        List<Holding> adjustedHoldings = new ArrayList<>();
        // Total Value of all holdings in the portfolio
        double totalValue = 0.0;

        // Assuming portfolioHoldings and
        if (p.getId() == portfolioId.getPortfolioId()) {
            for (int i = 0; i < portfolioHoldings.size(); i++) {
                // Each fund category
                double value = portfolioHoldings.get(i).price.getAmount() * portfolioHoldings.get(i).units;
                holdingValues.add(value);
                totalValue += value;
            }

            // Finding wanted value of each fund based on ps
            wantedValue = getWantedValue(totalValue, ps, portfolioHoldings);

            valuesToBuyOrSell = getAllocationDifferences(wantedValue, holdingValues);
            // See how many of each fund type to buy/sell
            for (int i = 0; i < valuesToBuyOrSell.size(); i++) {
                // price of the i^th
                double pricePerUnit = p.getHoldings().get(i).price.getAmount();
                // count to buy/sell per each type - negative for sell, vice versa

                double count = (valuesToBuyOrSell.get(i) / pricePerUnit);

                countToBuyOrSell.add(count);
            }
            // Creating new portfolio (rebalanced portfolio)
            for (int i = 0; i < p.getHoldings().size(); i++) {
                // Old Holding Info
                Holding h = getNewHolding(p, i, countToBuyOrSell);
                h.setPrice(p.getHoldings().get(i).getPrice());
                rebalancedHoldings.add(h);
            }
            // Total value of the funds in old portfolio
            Double oldPortValue = totalValue;
            // Total value of the funds in new portfolio
            Double newPortValue = getHoldingValue(rebalancedHoldings);
            // https://alvinalexander.com/java/how-to-round-float-double-to-int-integer-in-javazc
            // Check if rebalanced Portfolio has total value more than the old one
            // if(newPortValue > oldPortValue) {
            // adjustedHoldings = adjustHoldings(p.getHoldings(), rebalancedHoldings,
            // countToBuyOrSell);
            // Double adjustedPortValue = getHoldingValue(adjustedHoldings);
            // return makeRecommendation1(adjustedHoldings, p.getHoldings(), ps);
            // } else {
            return makeRecommendation1(rebalancedHoldings, p.getHoldings(), ps);
            // }
        } else {
            // Return PortfolioRecommendation with Transanction = null to catch later
            // TODO
            return new PortfolioRecommendation();
        }
    }

    /*
     * Handle the case where the total value of the post-rebalanced funds is more
     * than pre-rebalanced. Will be done by 1. Calculating the weighted sum of the
     * amount ($$) of the total funds that the rebalance algorithm want to sell 2.
     * Try to sell holdings such that the value sold is closest to weighted sum by
     * 2.1 Selling the cheapest fund type until the value of the rebalanced
     * portfolio is equal or less than the pre-rebalanced portfolio NOTE - assume
     * that the selling in 2.1 results in portfolio that is still within deviation
     * threshold Else, pick the second cheapest fund type and sell it. Repeat until
     * value of the portfolio is below pre-rebalanced portfolio
     * 
     * @param newPort portfolio that has been rebalanced
     * 
     * @param oldPort portfolio that has not been rebalanced
     * 
     * @param countToBuyOrSell list of amount of fund types to buy/sell
     */
    public static List<Holding> adjustHoldings(List<Holding> oldHoldings, List<Holding> newHoldings,
            List<Double> countToBuyOrSell) {
        double weightedSum = 0.0;
        for (int i = 0; i < oldHoldings.size(); i++) {

            // Amount that algorithm suggests to buy/sell - can & likely to be decimal
            double amountToBuyOrSell = countToBuyOrSell.get(i);
            // if it's to sell (negative)
            if (isNegative(amountToBuyOrSell)) {
                // price per each unit of fund type
                double price = oldHoldings.get(i).price.getAmount();
                weightedSum += price * amountToBuyOrSell;
            }
        }
        // find the cheapest fund type per unit
        Holding cheapestHolding = oldHoldings.get(0);
        double cheapestPrice = cheapestHolding.price.getAmount();
        for (int i = 0; i < oldHoldings.size(); i++) {
            double pricePerUnit = oldHoldings.get(i).price.getAmount();
            if (pricePerUnit < cheapestPrice) {
                cheapestPrice = pricePerUnit;
                cheapestHolding = oldHoldings.get(i);
            }
        }
        List<Integer> countToSell = new ArrayList<>();
        for (int i = 0; i < oldHoldings.size(); i++) {
            int count = 0;
            while (true) {
                double pricePerUnit = oldHoldings.get(i).price.getAmount();
                if ((count + 1) * pricePerUnit > Math.abs(weightedSum)) {
                    countToSell.add(count);
                    break;
                } else {
                    count++;
                }
            }
        }
        int index = 0;
        double smallestDifference = Double.MAX_VALUE;
        for (int i = 0; i < oldHoldings.size(); i++) {
            double minValue = countToSell.get(i) * oldHoldings.get(i).price.getAmount();
            double maxValue = (countToSell.get(i) + 1) * oldHoldings.get(i).price.getAmount();
            if (Math.abs(minValue - Math.abs(weightedSum)) < smallestDifference) {
                smallestDifference = Math.abs(minValue - Math.abs(weightedSum));
                index = i;
            }
            if (Math.abs(maxValue - Math.abs(weightedSum)) < smallestDifference) {
                smallestDifference = Math.abs(maxValue - Math.abs(weightedSum));
                index = i;
                countToSell.set(index, countToSell.get(index) + 1);

            }
        }
        // get adjusted list of Holdings
        List<Holding> adjustedHoldings = newHoldings;
        double oldBalance = adjustedHoldings.get(index).price.getAmount() * adjustedHoldings.get(index).units;
        int oldUnit = adjustedHoldings.get(index).units;
        double pricePerUnit = oldBalance / oldUnit;
        adjustedHoldings.get(index).units = adjustedHoldings.get(index).units - countToSell.get(index);
        adjustedHoldings.get(index).balance.setAmount(oldBalance - (pricePerUnit * countToSell.get(index)));

        return adjustedHoldings;
    }

    /*
     * 
     * TODO - add method for rebalancing with adding money
     */

    // HELPERS ////////////////////////////////////////////////////////////////////
    /*
     *
     *
     * Return a holding that the algorithm want to have
     */
    private static Holding getNewHolding(Portfolio p, int i, List<Double> countToBuyOrSell) {
        /*
         * Old Holding information
         */
        long id = p.getHoldings().get(i).getFundId();
        int unit = p.getHoldings().get(i).getUnits();
        double amount = p.getHoldings().get(i).getPrice().getAmount() * p.getHoldings().get(i).units;

        // new information
        int changeCount = (int) Math.round(countToBuyOrSell.get(i));
        Balance newBalance = new Balance((unit + changeCount) * amount / unit, "$");

        return new Holding(id, unit + changeCount, newBalance);
    }

    /*
    
     */
    private static PortfolioRecommendation makeRecommendation1(List<Holding> newHoldings, List<Holding> oldHoldings,
            PortfolioSettings ps) {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < newHoldings.size(); i++) {
            Transaction t = new Transaction();
            t.setFundId(oldHoldings.get(i).getFundId());
            int oldUnit = oldHoldings.get(i).units;
            int newUnit = newHoldings.get(i).units;
            if (newUnit >= oldUnit) {
                t.setAction("buy");
            } else {
                t.setAction("sell");
            }
            t.setUnits(Math.abs(newUnit - oldUnit));
            transactions.add(t);
        }

        PortfolioRecommendation pr = new PortfolioRecommendation(transactions);
        PortfolioIdentity pID = ps.getPortfolioIdentity();
        pr.setPortfolioIdentity(pID);
        return pr;
    }
    /*
    
     */

    private static double findMaxHoldingDifference(Portfolio p, PortfolioSettings ps) {
        // difference of the fund percentage in portfolio and target allocation
        double maxDifference = 0.0;

        // total value of all holdings in portfolio p
        double allBalance = 0.0;
        for (int i = 0; i < p.getHoldings().size(); i++) {
            allBalance += (p.getHoldings().get(i).price.getAmount() * p.getHoldings().get(i).units);
        }
        for (int i = 0; i < p.getHoldings().size(); i++) {
            long portFundID = p.getHoldings().get(i).fundId;

            Allocation matchingAllocation = ps.getAllocations().stream().filter(a -> a.getFundId() == portFundID)
                    .findFirst().orElse(null);

            if (matchingAllocation != null) {
                double portPercentage = 100
                        * (p.getHoldings().get(i).getPrice().getAmount() * p.getHoldings().get(i).getUnits())
                        / allBalance;
                double portSettingPercentage = matchingAllocation.getPercentage();
                double difference = Math.abs(portPercentage - portSettingPercentage);
                if (difference > maxDifference) {
                    maxDifference = difference;
                }
            }
        }
        return maxDifference;
    }

    /*
     * Check whether a double is negative or not
     * 
     * @param double d - double to check //
     * https://stackoverflow.com/questions/10399801/how-to-check-if-double-value-is-
     * negative-or-not
     */
    private static boolean isNegative(double d) {
        return Double.compare(d, 0.0) < 0;
    }

    /*
     * @param target - list of wanted percentage of the holdings to be in the
     * portfolio
     * 
     * @param current - list of current percentage of the holdings to be in the
     * portfolio
     * 
     * @return a Linked list of doubles - if negative, means that the fund type must
     * be sold to rebalance, vice versa
     */
    private static List<Double> getAllocationDifferences(List<Double> targets, List<Double> current) {
        List<Double> differences = new LinkedList<Double>();
        for (int i = 0; i < targets.size(); i++) {
            differences.add(targets.get(i) - current.get(i));
        }
        return differences;
    }

    /*
     * Return a List of percentage (decimal 0 - 1) of fund types in tthe portfolio
     * 
     * @param holdingValues - value of the fund types in the portfolio
     * 
     * @param total - total amount of all the funds in the portfolio
     */
    private static List<Double> calculatePercentage(List<Double> holdingValues, double total) {
        List<Double> holdingPercentage = new ArrayList<Double>();
        for (double d : holdingValues) {
            holdingPercentage.add(100 * d / total);
        }
        return holdingPercentage;
    }

    /*
     * Return a List of values ($$) that are required to buy/sell for each fund type
     * 
     * @param total - total amount of all the funds in the portfolio
     * 
     * @param ps - contains a preferred percentage for each fund type
     */
    private static List<Double> getWantedValue(double total, PortfolioSettings ps, List<Holding> holdings) {
        List<Double> wantedValue = new LinkedList<Double>();
        for (int i = 0; i < holdings.size(); i++) {
            long holdingFundId = holdings.get(i).getFundId();
            Allocation matchingAllocation = ps.getAllocations().stream().filter(a -> a.getFundId() == holdingFundId)
                    .findFirst().orElse(null);

            double percentage = matchingAllocation.getPercentage();
            wantedValue.add(percentage * total / 100);
        }
        return wantedValue;
    }

    /*
     * Return the value of all the funds in a portfolio
     * 
     * @param p - a portfolio to find the total value
     */
    private static double getHoldingValue(List<Holding> holdings) {
        double totalValue = 0;
        for (int i = 0; i < holdings.size(); i++) {
            totalValue += holdings.get(i).price.getAmount() * holdings.get(i).units;
        }
        return totalValue;
    }

}
