package com.crmintegration.demo.crmintegration.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.crmintegration.demo.crmintegration.client.ZillowClient;
import com.crmintegration.demo.crmintegration.client.ZohoClient;
import com.crmintegration.demo.crmintegration.dto.PropertyListingDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CrossStreetService {

    @Value("${last.zillow.api.call}")
    private String lastZillowApiCall;

    private final long zillowDataFetchTime = 120000;// 120000; // 2 minutes //900000; //15 minutes

    private final ZillowClient zillowClient;
    private final ZohoClient zohoClient;

    private static AtomicReference<LocalDateTime> lastCheckedTimeRef = new AtomicReference<>();

    public CrossStreetService(ZillowClient zillowClient, ZohoClient zohoClient) {
        this.zillowClient = zillowClient;
        this.zohoClient = zohoClient;
    }

    @PostConstruct
    public void fetchDataOnStartup() {
        // Parse the timestamp from the properties file
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime lastCheckedTime = LocalDateTime.parse(lastZillowApiCall, formatter);
        lastCheckedTimeRef.set(lastCheckedTime);

        // Call the Zillow API to fetch data inserted after the timestamp
        List<Object> newListings = zillowClient
                .getListingsAfterDate(lastCheckedTime.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        if (!newListings.isEmpty()) {
            // Handle new listings
            System.out.println("Found new listings: " + newListings.size());
            for (Object listing : newListings) {
                try {
                    // Convert LinkedHashMap to PropertyListingDTO
                    PropertyListingDTO propertyListing = objectMapper.convertValue(listing, PropertyListingDTO.class);
                    // Add to Zoho Accounts
                    zohoClient.addToZohoAccounts(propertyListing);
                    updateLastCheckedTime(LocalDateTime.now());
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle the error if the conversion fails
                }
            }
        } else {
            updateLastCheckedTime(LocalDateTime.now());
            System.out.println("No new listings found after: " + lastCheckedTime);
        }
    }

    @Scheduled(fixedRate = zillowDataFetchTime)
    public void checkForNewListings() {
        System.out.println("Checking for new listings...");

        // Fetch the last checked time
        LocalDateTime lastCheckedTime = lastCheckedTimeRef.get();

        // Call the Zillow API to fetch listings added after the last checked time
        List<Object> newListings = zillowClient.getListingsAfterDate(lastCheckedTime.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        if (!newListings.isEmpty()) {
            // Handle new listings
            System.out.println("Found new listings: " + newListings.size());
            for (Object listing : newListings) {
                try {
                    // Convert LinkedHashMap to PropertyListingDTO
                    PropertyListingDTO propertyListing = objectMapper.convertValue(listing, PropertyListingDTO.class);
                    // Add to Zoho Accounts
                    zohoClient.addToZohoAccounts(propertyListing);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle the error if the conversion fails
                }
            }
            updateLastCheckedTime(LocalDateTime.now());
        } else {
            System.out.println("No new listings found after: " + lastCheckedTime);
        }
    }

    private void updateLastCheckedTime(LocalDateTime newTime) {
        // Update the in-memory timestamp
        lastCheckedTimeRef.set(newTime);

        try {
            updatePropertiesFile(newTime);
            System.out.println("Updated the last.zillow.api.call timestamp to: " + newTime);
        } catch (IOException e) {
            System.out.println("Failed to update the application.properties file.");
            e.printStackTrace();
        }
    }

    private void updatePropertiesFile(LocalDateTime newTime) throws IOException {
        String newTimestamp = newTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String propertiesFilePath = "src/main/resources/application.properties";
        File propertiesFile = new File(propertiesFilePath);

        if (propertiesFile.exists()) {
            String content = new String(java.nio.file.Files.readAllBytes(propertiesFile.toPath()));
            content = content.replaceFirst("last.zillow.api.call=.*", "last.zillow.api.call=" + newTimestamp);

            FileWriter writer = new FileWriter(propertiesFile);
            writer.write(content);
            writer.close();
        }
    }
}
