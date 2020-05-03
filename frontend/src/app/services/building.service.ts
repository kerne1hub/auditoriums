import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {Building} from '../common/building';

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
}
