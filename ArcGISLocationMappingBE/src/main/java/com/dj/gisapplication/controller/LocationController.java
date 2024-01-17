package com.dj.gisapplication.controller;

import com.dj.gisapplication.model.Location;
import com.dj.gisapplication.service.LocationService;
import com.dj.gisapplication.service.PoiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;
	private PoiService PoiService;

    @Autowired
    public LocationController(LocationService locationService, PoiService poiService) { // Modify this constructor
        this.locationService = locationService;
        this.PoiService = poiService; // Initialize poiService
    }

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        Optional<Location> location = locationService.getLocationById(id);
        if(location.isPresent()) {
            return new ResponseEntity<>(location.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/random")
    public ResponseEntity<Location> addRandomLocation() {
        try {
            Location location = PoiService.getRandomLocation(); // Use the injected poiService here
            return new ResponseEntity<>(location, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle exceptions appropriately
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        try {
            locationService.deleteLocation(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log the exception details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
