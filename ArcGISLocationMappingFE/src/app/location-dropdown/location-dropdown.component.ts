import { Component, OnInit } from '@angular/core';
import { LocationService } from '../location.service';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Location } from '../location.model';
import * as L from 'leaflet';

@Component({
  selector: 'app-location-dropdown',
  templateUrl: './location-dropdown.component.html',
  styleUrl: './location-dropdown.component.css',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class LocationDropdownComponent implements OnInit {
  locations: any[] = [];  
  selectedLocationId: number | null = null;
  constructor(private locationService: LocationService, private http: HttpClient) { }


  ngOnInit(): void {
    this.locationService.getLocations().subscribe(locations => {
      this.locations = locations;
    },
    (error) => {
      console.error('error fetching locations:', error);
    });
  }

  onRandomize(): void {
    this.locationService.getRandomLocation().subscribe(
      location => {
        this.locations.push(location); // Add the new location to the dropdown
        this.selectedLocationId = location.id; // Save the id of the new location
        this.locationService.updateLocation(location); // Inform the map to update
      },
      error => console.error('Error fetching random location:', error)
    );
  }

  onLocationSelected(locationId: number): void {
  
    if (locationId) { // Check if the value is a truthy number
      this.selectedLocationId = locationId;
      this.locationService.getLocationById(locationId).subscribe(
        location => {
          this.locationService.updateLocation(location); // Notify other components
        },
        error => console.error('Error fetching location:', error)
      );
    } else {
      console.log('Invalid location ID or placeholder selected');
    }
  }
  
  
}