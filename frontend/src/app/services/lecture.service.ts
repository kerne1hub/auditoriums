import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {map} from 'rxjs/operators';
import {Lecture} from '../common/lecture';
import {Group} from '../common/group';
import {Subject} from '../common/subject';

@Injectable({
  providedIn: 'root'
})
export class LectureService {

  private baseUrl = 'http://localhost:8080/api/lectures';

  constructor(private httpClient: HttpClient) { }

  getActiveTab() {
    if (!localStorage.getItem('activeTab')) {
      localStorage.setItem('activeTab', 'profile');
    }
    return localStorage.getItem('activeTab');
  }

  setActiveTab(tab: string) {
    localStorage.setItem('activeTab', tab);
  }

  clearActiveTab() {
    localStorage.removeItem('activeTab');
  }

  addLecture(lectureDto: any) {
    return this.httpClient.post<Lecture>(this.baseUrl, lectureDto)
      .pipe(map(response => response));
  }

  getLectures(date: Date): Observable<Lecture[]> {
    return this.httpClient.get<Lecture[]>(this.baseUrl, { params: new HttpParams()
        .set('date', date.toLocaleDateString())})
      .pipe(map(response => response));
  }

  getLecturesByBuilding(buildingId: number, date: Date) {
    return this.httpClient.get<Lecture[]>(this.baseUrl, { params: new HttpParams()
        .set('date', date.toLocaleDateString())
        .set('buildingId', `${buildingId}`)})
      .pipe(map(response => response));
  }

  getLecturesByGroup(groupId: number | string, date: Date): Observable<Lecture[]> {
    return this.httpClient.get<Lecture[]>(this.baseUrl, { params: new HttpParams()
        .set('groupId', `${groupId}`)
        .set('date', date.toLocaleDateString())})
      .pipe(map(response => response));
  }

  getLecturesByLecturer(lecturerId: number | string, date: Date): Observable<Lecture[]> {
    return this.httpClient.get<Lecture[]>(this.baseUrl, { params: new HttpParams()
        .set('lecturerId', `${lecturerId}`)
        .set('date', date.toLocaleDateString())})
      .pipe(map(response => response));
  }

  getSubjects(term: string): Observable<Subject[]> {
    if (term === '') {
      return of([]);
    }

    return this.httpClient.get<Group[]>('http://localhost:8080/api/subjects', { params: new HttpParams()
        .set('name', term)})
      .pipe(map(response => response));
  }

  getUndefinedLectures(date: Date): Observable<Lecture[]> {
    return this.httpClient.get<Lecture[]>(this.baseUrl, { params: new HttpParams()
        .set('undefined', 'true')
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

  deleteLecture(id: number) {
    return this.httpClient.delete(this.baseUrl + `/${id}`);
  }
}
