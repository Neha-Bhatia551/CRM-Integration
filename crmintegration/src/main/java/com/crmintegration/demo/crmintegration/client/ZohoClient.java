package com.crmintegration.demo.crmintegration.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crmintegration.demo.crmintegration.dto.PropertyListingDTO;

@Component
public class ZohoClient {
    @Value("${zoho.accounts.module.api}")
    String zohoApiUrl;

    @Value("${zoho.access.token}")
    String authToken;

    public void addToZohoAccounts(PropertyListingDTO listing) {

        // Prepare JSON payload for Zoho API
        String payload = String.format(
                "{\n" +
                        "    \"data\": [\n" +
                        "        {\n" +
                        "            \"Account_Name\": \"Zillow\",\n" +
                        "            \"Billing_Street\": \"%s\",\n" +
                        "            \"Billing_Code\": \"%s\",\n" +
                        "            \"Billing_City\": \"%s\",\n" +
                        "            \"Billing_State\": \"%s\",\n" +
                        "            \"Rating\": \"%s\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"trigger\": [\"approval\", \"workflow\", \"blueprint\"]\n" +
                        "}",
                listing.getStreet(), // Billing_Street
                listing.getZipcode(), // Billing_Code
                listing.getCity(), // Billing_City
                listing.getState(), // Billing_State
                listing.getListedPrice() // Rating
        );

        try {
            // Make HTTP POST request to Zoho API
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(zohoApiUrl))
                    .header("Accept", "*/*")
                    .header("User-Agent", "Java Application")
                    .header("Authorization", "Zoho-oauthtoken " + authToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                System.out.println("Successfully added listing to Zoho Accounts: " + listing);
            } else {
                System.out.println("Failed to add listing to Zoho Accounts. Response: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Error while adding listing to Zoho Accounts: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
