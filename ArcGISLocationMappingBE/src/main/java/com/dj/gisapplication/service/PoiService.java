package com.dj.gisapplication.service;

import com.dj.gisapplication.model.Location;
import com.dj.gisapplication.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ThreadLocalRandom;
// Add necessary ArcGIS imports

@Service
public class PoiService {

    private final LocationRepository locationRepository;

    @Autowired
    public PoiService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getRandomLocation() {
        // Generate random coordinates
        double randomLatitude = getRandomLatitude();
        double randomLongitude = getRandomLongitude();

        // Simulate an API call to fetch location data
        Location location = callArcGISApi(randomLatitude, randomLongitude);

        // Save the new Location object to the database
        return locationRepository.save(location);
    }

    private double getRandomLatitude() {
        return ThreadLocalRandom.current().nextDouble(-90.0, 90.0);
    }

    private double getRandomLongitude() {
        return ThreadLocalRandom.current().nextDouble(-180.0, 180.0);
    }

    private Location callArcGISApi(double latitude, double longitude) {
        String apiKey = "AAPKa56ec99126c64eaf91a7188265ae7302TP9HsUg5LS1rDwF9Deu4ZErlRhWINCa_tx-XPYzIGCMYQRvgCAfkumq3m5q_qLbz";
        String url = "https://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer/reverseGeocode?location=" 
                     + longitude + "," + latitude 
                     + "&f=json&token=" + apiKey;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            String jsonResponse = EntityUtils.toString(httpClient.execute(request).getEntity());
            JsonNode rootNode = new ObjectMapper().readTree(jsonResponse);

            // Parse the JSON response
            JsonNode locationNode = rootNode.path("address");
            String placeName = locationNode.path("LongLabel").asText();
            String city = locationNode.has("City") && !locationNode.path("City").asText().isEmpty() 
                    ? locationNode.path("City").asText() 
                    : "Unknown City";
            String region = locationNode.has("Region") && !locationNode.path("Region").asText().isEmpty() 
                      ? locationNode.path("Region").asText() 
                      : "Unknown State";
            // Map the response to Location object
            Location location = new Location();
            location.setName(placeName);
            location.setDescription("Location at coordinates: " + latitude + ", " + longitude);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setCity(city); // Dummy data
            location.setState(region); // Dummy data
            // Set other Location fields as necessary

            return location;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
            return null;
        }
}
}
