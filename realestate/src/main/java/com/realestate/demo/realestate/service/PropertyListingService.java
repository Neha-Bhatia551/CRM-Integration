package com.realestate.demo.realestate.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.realestate.demo.realestate.dto.PropertyListingDTO;

import jakarta.annotation.PostConstruct;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PropertyListingService {

    private List<PropertyListingDTO> cachedListings = new ArrayList<>();
    private static final String CSV_FILE_PATH = "RealEstateData.csv";

    @PostConstruct
    public void loadListings() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        cachedListings = new ArrayList<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CSV_FILE_PATH);
                InputStreamReader reader = new InputStreamReader(inputStream);
                CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            // Loop through each record (row) in the CSV file
            for (CSVRecord record : parser) {
                PropertyListingDTO listing = new PropertyListingDTO();

                // Access the columns by index, assuming the order of columns matches
                listing.setState(record.get(0).trim()); // 1st column
                listing.setCity(record.get(1).trim()); // 2nd column
                listing.setStreet(record.get(2).trim()); // 3rd column
                listing.setZipcode(record.get(3).trim()); // 4th column

                listing.setBedroom(parseDouble(record.get(4))); // 5th column
                listing.setBathroom(parseDouble(record.get(5))); // 6th column
                listing.setArea(parseDouble(record.get(6))); // 7th column
                listing.setPricePerSquareFoot(parseDouble(record.get(7))); // 8th column
                listing.setLotArea(parseDouble(record.get(8))); // 9th column
                listing.setMarketEstimate(parseDouble(record.get(9))); // 10th column
                listing.setRentEstimate(parseDouble(record.get(10))); // 11th column

                listing.setLatitude(parseDouble(record.get(11))); // 12th column
                listing.setLongitude(parseDouble(record.get(12))); // 13th column
                listing.setListedPrice(parseDouble(record.get(13))); // 14th column

                // Parse createdAt and updatedAt from CSV, assuming the last two columns
                String createdAtStr = record.get(14).trim(); // 15th column (CreatedAt)
                String updatedAtStr = record.get(15).trim(); // 16th column (UpdatedAt)

                // Convert strings to LocalDateTime using the formatter
                LocalDateTime createdAt = LocalDateTime.parse(createdAtStr, formatter);
                LocalDateTime updatedAt = LocalDateTime.parse(updatedAtStr, formatter);

                listing.setCreatedAt(createdAt);
                listing.setUpdatedAt(updatedAt);
                cachedListings.add(listing);
            }
            System.out.println("Data successfully loaded into memory.");
        } catch (IOException e) {
            System.out.println("Data successfully not loaded into memory.");

            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 600000) // 10 minutes in milliseconds
    public void refreshCache() {
        System.out.println("Refreshing cache...");
        loadListings(); // Reuse the existing loading logic
        System.out.println("Cache refreshed.");
    }

    public List<PropertyListingDTO> getAllListings() {
        return cachedListings; // Return the cached data
    }

    public List<PropertyListingDTO> getLimitedListings(int limit) {
        return cachedListings.stream().limit(limit).toList();
    }

    private Double parseDouble(String value) {
        return (value == null || value.trim().isEmpty()) ? 0.0 : Double.valueOf(value.trim());
    }

    // New function to get listings added after a given timestamp
    public List<PropertyListingDTO> getListingsAfterTimestamp(LocalDateTime timestamp) {
        return cachedListings.stream()
                .filter(listing -> listing.getCreatedAt().isAfter(timestamp))
                .collect(Collectors.toList());
    }

    public void appendDataToCSV(List<PropertyListingDTO> newListings) {
        // Define the file path where you want to append
        String filePath = "src/main/resources/" + CSV_FILE_PATH; // Adjust this to your path
        LocalDateTime currentDateTime = LocalDateTime.now();

        try (FileWriter fileWriter = new FileWriter(filePath, true)) {
            // Write each new listing to the CSV file
            fileWriter.append("\n");
            for (PropertyListingDTO listing : newListings) {
                // Create a CSV record as a string
                listing.setCreatedAt(currentDateTime);
                listing.setUpdatedAt(currentDateTime);
                String record = String.join(",",
                        listing.getState(),
                        listing.getCity(),
                        listing.getStreet(),
                        listing.getZipcode(),
                        String.valueOf(listing.getBedroom()),
                        String.valueOf(listing.getBathroom()),
                        String.valueOf(listing.getArea()),
                        String.valueOf(listing.getPricePerSquareFoot()),
                        String.valueOf(listing.getLotArea()),
                        String.valueOf(listing.getMarketEstimate()),
                        String.valueOf(listing.getRentEstimate()),
                        String.valueOf(listing.getLatitude()),
                        String.valueOf(listing.getLongitude()),
                        String.valueOf(listing.getListedPrice()),
                        String.valueOf(listing.getCreatedAt()),
                        String.valueOf(listing.getUpdatedAt()));

                // Write the record followed by a newline character
                fileWriter.write(record + "\n");

                // After appending to CSV, add it to the cachedListings (in-memory list)
                cachedListings.add(listing);
            }

            System.out.println("New listings successfully appended to CSV and cached.");
        } catch (IOException e) {
            System.out.println("Error appending data to CSV file.");
            e.printStackTrace();
        }
    }
}
