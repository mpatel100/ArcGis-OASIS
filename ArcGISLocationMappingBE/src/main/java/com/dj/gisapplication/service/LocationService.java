package com.dj.gisapplication.service;

import com.dj.gisapplication.model.Location;
import com.dj.gisapplication.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }
    
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    // Additional methods can be added as needed
}
