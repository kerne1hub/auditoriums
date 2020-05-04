import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Auditorium } from '../common/auditorium';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuditoriumService {

  private baseUrl = 'http://localhost:8080/api/auditoriums';

  constructor(private httpClient: HttpClient) { }

  getAuditoriums(buildingId: number | string, date: Date): Observable<Auditorium[]> {
    return this.httpClient.get<Auditorium[]>(this.baseUrl, {params: new HttpParams()
        .set('buildingId', `${buildingId}`)
        .set('date', date.toLocaleDateString())})
      .pipe(map(response => response));
  }
}
