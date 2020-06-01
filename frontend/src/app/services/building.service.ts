import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {Building} from '../common/building';
import {Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BuildingService {

  private baseUrl = 'http://localhost:8080/api/buildings';

  constructor(private httpClient: HttpClient) { }

  addBuilding(building: Building): Observable<Building> {
    return this.httpClient.post<Building>(this.baseUrl, building)
      .pipe(map( response => response));
  }

  deleteBuilding(id: number) {
    return this.httpClient.delete(this.baseUrl + `/${id}`);
  }

  editBuilding(buildingId: number, building: Building): Observable<Building> {
    return this.httpClient.put<Building>(this.baseUrl + `/${buildingId}`, building)
      .pipe(map(response => response));
  }

  getBuildings() {
    return this.httpClient.get<Building[]>(this.baseUrl)
      .pipe(map(response => response));
  }

  getBuildingsByName(term: string) {
    if (term === '') {
      return of([]);
    }

    return this.httpClient.get<Building[]>(this.baseUrl, { params: new HttpParams()
        .set('name', term)})
      .pipe(map(response => response));
  }

}
