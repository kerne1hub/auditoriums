import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {map} from 'rxjs/operators';
import {Group} from '../common/group';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  private baseUrl = 'http://localhost:8080/api/groups';

  constructor(private httpClient: HttpClient) { }

  addGroup(group: Group): Observable<Group> {
    return this.httpClient.post<Group>(this.baseUrl, group)
      .pipe(map( response => response));
  }

  deleteGroup(id: number) {
    return this.httpClient.delete(this.baseUrl + `/${id}`);
  }

  editGroup(groupId: number, group: Group): Observable<Group> {
    return this.httpClient.put<Group>(this.baseUrl + `/${groupId}`, group)
      .pipe(map(response => response));
  }

  getGroups() {
    return this.httpClient.get<Group[]>(this.baseUrl)
      .pipe(map(response => response));
  }

  getGroupsByName(term: string) {
    if (term === '') {
      return of([]);
    }

    return this.httpClient.get<Group[]>(this.baseUrl, { params: new HttpParams()
        .set('name', term)})
      .pipe(map(response => response));
  }
}
