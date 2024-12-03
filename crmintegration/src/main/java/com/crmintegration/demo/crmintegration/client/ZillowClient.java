package com.crmintegration.demo.crmintegration.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.crmintegration.demo.crmintegration.dto.PropertyListingDTO;

import java.util.List;

@Component
public class ZillowClient {

    @Value("${zillow.hostname}")
    private String hostname;

    @Value("${zillow.api.getListingsAfterDate}")
    private String getListingsAfterDate;

    private RestTemplate restTemplate = new RestTemplate();

    // Method to fetch listings after a specific date
    public List<Object> getListingsAfterDate(String datetime) {
        String url = hostname + getListingsAfterDate + datetime;
        return restTemplate.getForObject(url, List.class);
    }
}
