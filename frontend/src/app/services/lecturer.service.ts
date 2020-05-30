import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Lecturer} from '../common/lecturer';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class LecturerService {

  constructor(private httpClient: HttpClient) { }

  getLecturer(id: number): Observable<Lecturer> {
    return this.httpClient.get<Lecturer>(`http://localhost:8080/api/lecturers/${id}`).pipe(
      map(response => response)
    );
  }

  getLecturers(keyword: string): Observable<Lecturer[]> {
    return this.httpClient.get<Lecturer[]>(`http://localhost:8080/api/lecturers?name=${keyword}`).pipe(
      map(response => response)
    );
  }


  editLecturer(lecturer: Lecturer) {
    return this.httpClient.put<Lecturer>(`http://localhost:8080/api/lecturers/${lecturer.id}`, lecturer).pipe(
      map(response => response)
    );
  }
}
