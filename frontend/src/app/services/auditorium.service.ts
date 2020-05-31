import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Auditorium} from '../common/auditorium';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuditoriumService {

  private baseUrl = 'http://localhost:8080/api/auditoriums';

  constructor(private httpClient: HttpClient) { }

  getAuditoriumsWithLectures(buildingId: number | string, date: Date): Observable<Auditorium[]> {
    return this.httpClient.get<Auditorium[]>(this.baseUrl, {params: new HttpParams()
        .set('buildingId', `${buildingId}`)
        .set('date', date.toLocaleDateString())})
      .pipe(map(response => response));
  }

  getAuditoriumsByBuilding(buildingId: number) {
    return this.httpClient.get<Auditorium[]>(this.baseUrl, {params: new HttpParams()
        .set('buildingId', `${buildingId}`)
        .set('details', 'false')})
      .pipe(map(response => response));
  }

  getAuditoriumsByName(term: string): Observable<Auditorium[]> {
    if (term === '') {
      return of([]);
    }

    return this.httpClient.get<Auditorium[]>('http://localhost:8080/api/auditoriums', { params: new HttpParams()
        .set('name', term)})
      .pipe(map(response => response));
  }

  addAuditorium(auditoriumDto: any) {
    return this.httpClient.post<Auditorium>(this.baseUrl, auditoriumDto)
      .pipe(map(response => response));
  }

  editAuditorium(id: number, auditoriumDto: any) {
    return this.httpClient.put<Auditorium>(this.baseUrl + `/${id}`, auditoriumDto)
      .pipe(map(response => response));
  }

  deleteAuditorium(id: number) {
    return this.httpClient.delete(this.baseUrl + `/${id}`);
  }
}
