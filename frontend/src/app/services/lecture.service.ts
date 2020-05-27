import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {map} from 'rxjs/operators';
import {Lecture} from '../common/lecture';
import {Group} from '../common/group';

@Injectable({
  providedIn: 'root'
})
export class LectureService {

  private baseUrl = 'http://localhost:8080/api/lectures';

  constructor(private httpClient: HttpClient) { }

  getLectures(groupId: number | string, date: Date): Observable<Lecture[]> {
    return this.httpClient.get<Lecture[]>(this.baseUrl, { params: new HttpParams()
        .set('groupId', `${groupId}`)
        .set('date', date.toLocaleDateString())})
      .pipe(map(response => response));
  }

  getGroups(term: string): Observable<Group[]> {
    if (term === '') {
      return of([]);
    }

    return this.httpClient.get<Group[]>('http://localhost:8080/api/groups', { params: new HttpParams()
        .set('name', term)})
      .pipe(map(response => response));
  }

  editLecture(lectureId: number, lectureDto: any) {
    return this.httpClient.put<Lecture>(this.baseUrl + `/${lectureId}`, lectureDto)
      .pipe(map(response => response));
  }
}
