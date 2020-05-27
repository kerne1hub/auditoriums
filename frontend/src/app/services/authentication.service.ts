import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { User } from '../common/user';
import {Lecturer} from '../common/lecturer';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  login(loginOrEmail: string, password: string) {
    const credentials = loginOrEmail.includes('@')? { email:loginOrEmail, password } : { login:loginOrEmail, password };

    return this.http.post<any>('http://localhost:8080/api/auth/login', credentials)
      .pipe(map(response => {
        // store user details and jwt token in local storage to keep user logged in between page refreshes
        localStorage.setItem('currentUser', JSON.stringify(response.user));
        localStorage.setItem('aa_token', response.token);
        this.currentUserSubject.next(response.user);
        return response.user;
      }));
  }

  logout() {
    // remove user from local storage and set current user to null
    localStorage.removeItem('currentUser');
    localStorage.removeItem('aa_token');
    this.currentUserSubject.next(null);
    location.reload();
  }

  registerLecturer(lecturer: Lecturer) {
    return this.http.post<Lecturer>('http://localhost:8080/api/lecturers', lecturer)
      .pipe(map(response => response))
  }
}
