import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Auditorium } from '../common/auditorium';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuditoriumService {

  private baseUrl = 'http://localhost:8080/api/auditoriums';

  constructor(private httpClient: HttpClient) { }

  getAuditoriums(): Observable<Auditorium[]> {
    return this.httpClient.get<Auditorium[]>(this.baseUrl)
      .pipe(map(response => response));
  }
}
