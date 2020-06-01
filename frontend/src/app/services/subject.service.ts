import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {map} from 'rxjs/operators';
import {Subject} from '../common/subject';

@Injectable({
  providedIn: 'root'
})
export class SubjectService {

  private baseUrl = 'http://localhost:8080/api/subjects';

  constructor(private httpClient: HttpClient) { }

  addSubject(subject: Subject): Observable<Subject> {
    return this.httpClient.post<Subject>(this.baseUrl, subject)
      .pipe(map( response => response));
  }

  deleteSubject(id: number) {
    return this.httpClient.delete(this.baseUrl + `/${id}`);
  }

  editSubject(subjectId: number, subject: Subject): Observable<Subject> {
    return this.httpClient.put<Subject>(this.baseUrl + `/${subjectId}`, subject)
      .pipe(map(response => response));
  }

  getSubjects() {
    return this.httpClient.get<Subject[]>(this.baseUrl)
      .pipe(map(response => response));
  }

  getSubjectsByName(term: string) {
    if (term === '') {
      return of([]);
    }

    return this.httpClient.get<Subject[]>(this.baseUrl, { params: new HttpParams()
        .set('name', term)})
      .pipe(map(response => response));
  }
}
