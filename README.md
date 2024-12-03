# CRM Customization & API Integration - Real Estate Data Integration

## Overview
This project demonstrates how to integrate real estate property data into Zoho CRM using a custom service and APIs. The goal of the project was to build a simple solution that allows Zoho CRM to track property listings from a real estate service, automate the process of fetching new listings, and push them into Zoho CRM’s accounts module.


## Key Features
**Real Estate Service API Simulation:** Mimics a real estate service's property listings by reading data from a CSV file (sample data sourced from Kaggle).

**API Service:** Exposes several endpoints to interact with property listings data.
1. **getAllListings:** Retrieves all listings.
2. **getLimitedListings:** Retrieves a limited number of listings.
3. **addListings:** Adds new listings to the CSV file.
4. **getListingsAfterDate:** Fetches listings that were added after a specified date.

**Data Integration:** Integrates with Zoho CRM by pushing new property listings into the CRM’s accounts module.

**Scheduled Fetching:** Calls the real estate service API every 15 minutes to check for new property listings and automatically pushes them to Zoho CRM if any new listings are found.

## Technologies Used
**Zoho CRM:** Used for managing the property listings data.

**API Development:** Java (Spring Boot) for building the custom service that interacts with the real estate service and Zoho CRM.

**CSV File:** Used for storing property data (sample data from Kaggle).

**Scheduling:** Spring's @Scheduled annotation for running the job every 15 minutes.

## How It Works
**Real Estate Service API:** A custom API service simulates data fetched from a real estate service, which reads data from a CSV file. This data includes property listings with attributes like price, location, and type.

**Zoho CRM Integration:** The application integrates with Zoho CRM's accounts module by using Zoho CRM's API to push the new property listings to the CRM.

**Scheduled Data Fetch**: Every 15 minutes, the application queries the real estate service API for new listings that have been added since the last check. If any new listings are found, they are added to Zoho CRM.

## Setup and Installation
Java 17 or later
Spring Boot
Zoho CRM API credentials

