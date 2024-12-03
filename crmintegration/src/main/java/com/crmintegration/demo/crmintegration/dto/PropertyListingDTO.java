package com.crmintegration.demo.crmintegration.dto;

import java.time.LocalDateTime;

public class PropertyListingDTO {

    private String state;
    private String city;
    private String street;
    private String zipcode;
    private double bedroom;
    private double bathroom;
    private double area; // Area in square feet
    private double pricePerSquareFoot; // PPSq
    private double lotArea;
    private Double marketEstimate; // Nullable
    private Double rentEstimate; // Nullable
    private double latitude;
    private double longitude;
    private double listedPrice;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public double getBedroom() {
        return bedroom;
    }

    public void setBedroom(double bedroom) {
        this.bedroom = bedroom;
    }

    public double getBathroom() {
        return bathroom;
    }

    public void setBathroom(double bathroom) {
        this.bathroom = bathroom;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getPricePerSquareFoot() {
        return pricePerSquareFoot;
    }

    public void setPricePerSquareFoot(double pricePerSquareFoot) {
        this.pricePerSquareFoot = pricePerSquareFoot;
    }

    public double getLotArea() {
        return lotArea;
    }

    public void setLotArea(double lotArea) {
        this.lotArea = lotArea;
    }

    public Double getMarketEstimate() {
        return marketEstimate;
    }

    public void setMarketEstimate(Double marketEstimate) {
        this.marketEstimate = marketEstimate;
    }

    public Double getRentEstimate() {
        return rentEstimate;
    }

    public void setRentEstimate(Double rentEstimate) {
        this.rentEstimate = rentEstimate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getListedPrice() {
        return listedPrice;
    }

    public void setListedPrice(double listedPrice) {
        this.listedPrice = listedPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
