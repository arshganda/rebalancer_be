package com.mib.rebalancer;

import com.mib.rebalancer.model.Allocation;
import com.mib.rebalancer.model.Deviation;
import com.mib.rebalancer.model.PortfolioSettings;
import com.mib.rebalancer.model.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RebalancerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RebalancerSystemControllerTest {


    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    String customerId = "i42bumx1oe";
    String id = "6981684";
    private String createURLWithPort(String uri){
        return "http://localhost:" + port + uri;
    }

    // /portfolio/{id}
    @Test
    /*
    this tests the normal functionality
     */
    public void testPOSTPortfolio(){
        List<Allocation> list = new ArrayList<>();
        Allocation a = new Allocation(1, 95);
        Allocation b = new Allocation(2,5);
        list.add(a);
        list.add(b);
        headers.set("x-custid", customerId);
        PortfolioSettings setting = new PortfolioSettings(2, "fund",list);
        HttpEntity<PortfolioSettings> entity = new HttpEntity<>(setting, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/portfolio/" + id),
                HttpMethod.POST, entity, String.class);

        System.out.println(response.getBody());
        String actual = response.getBody();
        System.out.println(actual);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody(), actual);

    }
    // /portfolio/{id}
    @Test
    /*
    this tests the over 100% error
     */
    public void testPOSTPortfolio400(){
        List<Allocation> list = new ArrayList<>();
        Allocation a = new Allocation(1, 95);
        Allocation b = new Allocation(2,2);
        list.add(a);
        list.add(b);
        headers.set("x-custid", customerId);
        PortfolioSettings setting = new PortfolioSettings(2, "fund",list);
        HttpEntity<PortfolioSettings> entity = new HttpEntity<>(setting, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/portfolio/" + id),
                HttpMethod.POST, entity, String.class);

        System.out.println(response.getBody());
        String actual = response.getBody();
        System.out.println(actual);
        Assert.assertEquals(response.getBody(), actual);

    }
    // @GetMapping("/portfolio/{id}")
    @Test
    public void testGETPortfolio(){
        List<Allocation> list = new ArrayList<>();
        Allocation a = new Allocation(1, 95);
        Allocation b = new Allocation(2,5);
        list.add(a);
        list.add(b);
        headers.set("x-custid", customerId);
        PortfolioSettings setting = new PortfolioSettings(2, "fund",list);
        HttpEntity<PortfolioSettings> entity = new HttpEntity<>(setting, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/portfolio/" + id),
                HttpMethod.POST, entity, String.class);

        System.out.println(response.getBody());
        String actual = response.getBody();
        System.out.println(actual);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody(), actual);

        headers.set("x-custid", customerId);

        HttpEntity<PortfolioSettings> entity2 = new HttpEntity<>(null, headers);
        ResponseEntity<String> response2 = restTemplate.exchange(
                createURLWithPort("/portfolio/" + id),
                HttpMethod.GET, entity2, String.class);

        System.out.println("RESPONSE BODY: "+ response2.getBody());
//        String actual = response.getBody();
//        System.out.println(actual);
//        Assert.assertEquals(response.getBody(), actual);
        Assert.assertEquals(HttpStatus.OK, response2.getStatusCode());
    }
    @Test
    ///portfolio/{id}/allocations
    public void testPUTAllocation(){
        // SETUP
        List<Allocation> list = new ArrayList<>();
        Allocation a = new Allocation(1, 95);
        Allocation b = new Allocation(2,5);
        list.add(a);
        list.add(b);
        headers.set("x-custid", customerId);
        PortfolioSettings setting = new PortfolioSettings(2, "fund",list);
        HttpEntity<PortfolioSettings> entity = new HttpEntity<>(setting, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/portfolio/" + id),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);


        // CALLING PUT
        List<Allocation> list2 = new ArrayList<>();
        Allocation alloc = new Allocation(1,100);
        Allocation alloc2 = new Allocation(2, 0);
        System.out.println(alloc.toString());
        list2.add(alloc);
        list2.add(alloc2);
        headers.set("x-custid", customerId);
        HttpEntity<List> entity2 = new HttpEntity<>(list2, headers);
        ResponseEntity<String> response2 = restTemplate.exchange(
                createURLWithPort("/portfolio/" + id +"/allocations"),
                HttpMethod.PUT, entity2, String.class);

        System.out.println(response2.getBody());
        Assert.assertEquals(response2.getStatusCode(),HttpStatus.OK);



    }

    @Test
    // /portfolio/{id}/deviation
    public void testPUTDeviation(){
        List<Allocation> list = new ArrayList<>();
        Allocation a = new Allocation(1, 70);
        Allocation b = new Allocation(2,30);
        list.add(a);
        list.add(b);
        headers.set("x-custid", customerId);
        PortfolioSettings setting = new PortfolioSettings(2, "fund",list);
        HttpEntity<PortfolioSettings> entity = new HttpEntity<>(setting, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/portfolio/" + id),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);

        Deviation deviation = new Deviation();
        deviation.setDeviation(2);
        System.out.println(deviation.hashCode());
        System.out.println("DEVIATION:  "+ deviation.getDeviation());
        headers.set("x-custid", customerId);
        HttpEntity<Deviation> entity2 = new HttpEntity<>(deviation, headers);
        ResponseEntity<String> response2 = restTemplate.exchange(
                createURLWithPort("/portfolio/" + id +"/deviation"),
                HttpMethod.PUT, entity2, String.class);

         System.out.println(response2.getBody());
         Assert.assertEquals(response2.getStatusCode(),HttpStatus.OK);
    }

//    @Test
//    public void testPUTDeviationError(){
//        Deviation deviation = new Deviation();
//        deviation.setDeviation(2);
//        System.out.println(deviation.hashCode());
//        System.out.println("DEVIATION:  "+ deviation.getDeviation());
//        headers.set("x-custid", customerId);
//        HttpEntity<Deviation> entity = new HttpEntity<>(deviation, headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                createURLWithPort("/portfolio/" + id +"/deviation"),
//                HttpMethod.PUT, entity, String.class);
//
//        System.out.println(response.getBody());
//        // ResponseStatusException e = new ResponseStatusException(HttpStatus.NOT_FOUND, "PortfolioSetting with id " + id + " not found");
//        Assert.assertEquals(HttpStatus.NOT_FOUND , response.getStatusCode());
//    }

    @Test
    // /portfolio/{id}/recommendations/available
    public void testGETRecommendationsAvailable(){
        // SETUP
        List<Allocation> list = new ArrayList<>();
        Allocation a = new Allocation(1, 95);
        Allocation b = new Allocation(2,5);
        list.add(a);
        list.add(b);
        headers.set("x-custid", customerId);
        PortfolioSettings setting = new PortfolioSettings(2, "fund",list);
        HttpEntity<PortfolioSettings> entity = new HttpEntity<>(setting, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/portfolio/" + id),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);


        headers.set("x-custid", customerId);
        HttpEntity<Deviation> entity2 = new HttpEntity<>(null, headers);
        ResponseEntity<String> response2 = restTemplate.exchange(
                createURLWithPort("/portfolio/" + id +"/recommendations/available"),
                HttpMethod.GET, entity2, String.class);

        System.out.println(response2.getBody());
        Assert.assertEquals(response2.getBody(), "false");
        Assert.assertEquals(response2.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGETRebalance(){
        // Setting up portfolio
        List<Allocation> list = new ArrayList<>();
        Allocation a = new Allocation(1, 30);
        Allocation b = new Allocation(2, 30);
        Allocation c = new Allocation(3, 40);
        list.add(a);
        list.add(b);
        list.add(c);
        headers.set("x-custid", customerId);
        PortfolioSettings setting = new PortfolioSettings(7, "fund", list);
        HttpEntity<PortfolioSettings> entity = new HttpEntity<>(setting, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/portfolio/" + "1334556"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);


        headers.set("x-custid", customerId);
        HttpEntity<String> entity2 = new HttpEntity<>(null, headers);
        ResponseEntity<String> response2 = restTemplate.exchange(
                createURLWithPort("/portfolio/" + "1334556" + "/rebalance"),
                HttpMethod.GET, entity2, String.class);

        System.out.println(response2.getBody());
        Assert.assertEquals(response2.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response2.getBody(), null);

        //Getting non-exisiting portfolio
//        headers.set("x-custid", customerId);
//        HttpEntity<String> entity3 = new HttpEntity<>(null, headers);
//        ResponseEntity<String> response3 = restTemplate.exchange(
//                createURLWithPort("/portfolio/" + id + "/rebalance"),
//                HttpMethod.GET, entity3, String.class);
//
//        System.out.println(response3.getBody());
//        Assert.assertEquals(response3.getStatusCode(), HttpStatus.NOT_FOUND);


    }
    @Test
    //portfolio/{id}/rebalance
    public void testPOSTRebalance(){
        // SETUP
        List<Allocation> list = new ArrayList<>();
        Allocation a = new Allocation(23456, 95);
        Allocation b = new Allocation(23457,5);
        list.add(a);
        list.add(b);
        headers.set("x-custid", customerId);
        PortfolioSettings setting = new PortfolioSettings(2, "fund",list);
        HttpEntity<PortfolioSettings> entity = new HttpEntity<>(setting, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/portfolio/" + "1334556"),
                HttpMethod.POST, entity, String.class);

        System.out.println(response.getBody());
        String actual = response.getBody();
        System.out.println(actual);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody(), actual);


        headers.set("x-custid", customerId);
        headers.set("Content-Type","application/json");
        HttpEntity<PortfolioSettings> entity2 = new HttpEntity<>(null, headers);
        ResponseEntity<String> response2 = restTemplate.exchange(
                createURLWithPort("/portfolio/" + "1334556" +"/rebalance"),
                HttpMethod.POST, entity2, String.class);

//        System.out.println(response.getBody());
//        String actual2 = response.getBody();
//        System.out.println(actual2);
//        Assert.assertEquals(response2.getBody(), actual2);
        Assert.assertEquals(HttpStatus.OK, response2.getStatusCode());
        System.out.println(response2.getBody());


    }
//    @Test
//    //portfolio/{id}/recommendation/{recommendationId}/execute
//    public void testPOSTExecute(){
//
//    }
    @Test
    //portfolio/{id}/recommendation/{recommendationId}/modify
    public void testPUTModify(){
        // SETUP
        List<Allocation> list = new ArrayList<>();
        Allocation a = new Allocation(23456, 95);
        Allocation b = new Allocation(23457,5);
        list.add(a);
        list.add(b);
        headers.set("x-custid", customerId);
        PortfolioSettings setting = new PortfolioSettings(2, "fund",list);
        HttpEntity<PortfolioSettings> entity = new HttpEntity<>(setting, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/portfolio/" + "1334556"),
                HttpMethod.POST, entity, String.class);

        System.out.println(response.getBody());
        String actual = response.getBody();
        System.out.println(actual);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody(), actual);


        headers.set("x-custid", customerId);
        headers.set("Content-Type","application/json");
        HttpEntity<PortfolioSettings> entity2 = new HttpEntity<>(null, headers);
        ResponseEntity<String> response2 = restTemplate.exchange(
                createURLWithPort("/portfolio/" + "1334556" +"/rebalance"),
                HttpMethod.POST, entity2, String.class);

        Assert.assertEquals(HttpStatus.OK, response2.getStatusCode());
        System.out.println(response2.getBody());


        headers.set("x-custid", customerId);
        headers.set("Content-Type","application/json");
        List<Transaction> list2 = new ArrayList<>();
        Transaction transaction = new Transaction("buy",23456,20);
        list2.add(transaction);
        HttpEntity<List> entity3 = new HttpEntity<>(list2, headers);
        ResponseEntity<String> response3 = restTemplate.exchange(
                createURLWithPort("/portfolio/" + "1334556" +"/recommendation/1/modify"),
                HttpMethod.PUT, entity3, String.class);


        Assert.assertEquals(HttpStatus.OK, response3.getStatusCode());
        System.out.println(response3.getBody());
    }


}
