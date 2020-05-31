import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {Building} from '../common/building';
import {of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BuildingService {

  private baseUrl = 'http://localhost:8080/api/buildings';

  constructor(private httpClient: HttpClient) { }

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
