import { Component, OnInit } from '@angular/core';
import { LocationService } from '../location.service';
import { CommonModule } from '@angular/common';
import * as L from 'leaflet';
import { HttpClient } from '@angular/common/http';
import { Location } from '../location.model';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrl: './map.component.css',
  standalone: true,
  imports: [CommonModule]
})
export class MapComponent implements OnInit {
  private map: L.Map | any;
  selectedLocation: Location | null = null;
  constructor(
    private locationService: LocationService,
    // Inject HttpClient if you're fetching directly from the component
    private http: HttpClient
  ) { }

  ngOnInit(): void {
    this.initMap();

    this.locationService.getLocationUpdateListener().subscribe(
      location => {
        this.centerMapOnLocation(location.id);
        this.displayLocationDetails(location);
      }
    );
  }

  private initMap(): void {
    this.map = L.map('map', {
      center: [ 39.8282, -98.5795 ], // Center of the map
      zoom: 3 // Initial zoom level
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap'
    }).addTo(this.map);
  }

  private displayLocationDetails(location: Location): void {
    this.selectedLocation = location;
  }

private addMarker(location: Location): void {
  if (this.map) {
    const latLng = L.latLng(location.latitude, location.longitude);
    const marker = L.marker(latLng, {icon: L.icon({
      iconUrl: '../../assets/redmarker.png', // Path to your red marker icon
      iconSize: [30, 32], // Size of the icon
      iconAnchor: [12, 41], // Point of the icon which will correspond to marker's location
      popupAnchor: [1, -34], // Point from which the popup should open relative to the iconAnchor
    })});
    marker.addTo(this.map).bindPopup(location.name);
    this.map.setView(latLng, 13); // Adjust zoom level as needed
  }
}

  private centerMapOnLocation(locationId: number): void {
    // Fetch the location details from the backend
    this.locationService.getLocationById(locationId).subscribe({
      next: (location) => {
        if (location && this.map) {
          const latLng = L.latLng(location.latitude, location.longitude);
          this.map.setView(latLng, 13); // '13' is the zoom level you want
          this.addMarker(location);
        }
      },
      error: (error) => {
        console.error('Error fetching location', error);
      }
    });
    
  }

}
