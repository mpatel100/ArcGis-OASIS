import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Observable, Subject, tap } from 'rxjs';
import { Location } from './location.model';

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  private locationUpdated = new Subject<any>();
  private apiUrl = 'http://localhost:8080'; 

  constructor(private http: HttpClient) { }

  getLocations() {
    return this.http.get<Location[]>('http://localhost:8080/api/locations');
  }

  updateLocation(location: Location) {
    this.locationUpdated.next(location);
  }

  getRandomLocation(): Observable<Location> {
    return this.http.get<Location>(`${this.apiUrl}/api/locations/random`).pipe(
      tap(location => {
        this.locationUpdated.next(location);
      })
    );
  }

  getLocationUpdateListener() {
    return this.locationUpdated.asObservable();
  }

  getLocationById(id: number): Observable<Location> {
    // Replace 'your-api-url' with the actual URL to your backend
    return this.http.get<any>(`http://localhost:8080/api/locations/${id}`);
  }
}
