package com.realestate.demo.realestate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realestate.demo.realestate.dto.PropertyListingDTO;
import com.realestate.demo.realestate.service.PropertyListingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/zillow/api")
public class DemoController {

    @Autowired
    private PropertyListingService propertyListingService;

    @GetMapping("getAllListings")
    public List<PropertyListingDTO> getAllListings() {
        return propertyListingService.getAllListings();
    }

    @GetMapping("getLimitedListings")
    public List<PropertyListingDTO> getLimitedListings(@RequestParam(defaultValue = "2") int limit) {
        return propertyListingService.getLimitedListings(limit);
    }

    // POST endpoint to add new listings to the CSV file
    @PostMapping("addListings")
    public String addListings(@RequestBody List<PropertyListingDTO> listings) {
        try {
            // Append listings to the CSV file
            propertyListingService.appendDataToCSV(listings);

            return "Listings added successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to add listings.";
        }
    }

    @GetMapping("getListingsAfterDate")
    public List<PropertyListingDTO> getListingsAfterDate(@RequestParam("datetime") String datetime) {
        try {
            // Parse the datetime parameter to LocalDateTime
            LocalDateTime parsedDateTime = LocalDateTime.parse(datetime);

            // Fetch all listings from the service
            List<PropertyListingDTO> allListings = propertyListingService.getAllListings();

            // Filter the listings created after the given datetime
            return allListings.stream()
                    .filter(listing -> listing.getCreatedAt().isAfter(parsedDateTime))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            // Return an empty list or handle error as needed
            return List.of();
        }
    }
}
