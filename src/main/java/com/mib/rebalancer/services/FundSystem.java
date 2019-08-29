package com.mib.rebalancer.services;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;

import java.util.Arrays;
import java.util.List;

import com.mib.rebalancer.model.Fund;
import com.mib.rebalancer.model.Portfolio;
import com.mib.rebalancer.model.TransactionRequest;
import com.mib.rebalancer.model.TransactionResponse;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Interfaces with mock system
 */

@Service
@Configurable(autowire = Autowire.BY_TYPE)
public class FundSystem {
    private String endpoint;

    public FundSystem() {
        this("");
    }

    public FundSystem(String endpoint) {
        this.endpoint = endpoint;
    }

    private static RestTemplate getTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        converter.setSupportedMediaTypes(Arrays.asList(TEXT_HTML, APPLICATION_JSON));
        restTemplate.getMessageConverters().add(converter);
        return restTemplate;
    }

    private HttpEntity getCustomerHeader(String custId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-custid", custId);
        headers.set("Content-Type", "application/json");

        return new HttpEntity<>("parameters", headers);
    }

    private HttpEntity getCustomerHeader(Object body, String custId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-custid", custId);
        headers.set("Content-Type", "application/json");

        return new HttpEntity<>(body, headers);
    }

    public List<Portfolio> getPortfolios(String custId) {
        ResponseEntity<Portfolio[]> responseEntity = getTemplate().exchange(endpoint + "/portfolios2", HttpMethod.GET,
                getCustomerHeader(custId), Portfolio[].class);

        return Arrays.asList(responseEntity.getBody());
    }

    public List<Fund> getFunds() {
        return Arrays.asList(getTemplate().getForObject(endpoint + "/funds2", Fund[].class));
    }

    public Fund getFund(int fundId) {
        return getTemplate().getForObject(endpoint + "/fund2/" + fundId, Fund.class);
    }

    public TransactionResponse postTransaction(String custId, TransactionRequest request) {
        try {
            ResponseEntity<TransactionResponse> responseEntity = getTemplate().exchange(endpoint + "/transaction2",
                    HttpMethod.POST, getCustomerHeader(request, custId), TransactionResponse.class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            return null;
        }
    }
}
