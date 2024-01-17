export interface Location {
    id: number;
    name: string;
    description: string;
    latitude: number;
    longitude: number;
    city: string;
    state: string;
    // Add any other fields that are returned by your backend and should be part of a location
  }