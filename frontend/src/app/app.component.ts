import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from './common/user';
import { AuthenticationService } from './services/authentication.service';
import { AlertService } from './services/alert.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  currentUser: User;

  constructor(private router: Router,
              private authenticationService: AuthenticationService,
              private alertService: AlertService) {
    this.authenticationService.currentUser.subscribe(
      user => this.currentUser = user
    )
  }

  getLinkState(url: string) {
    if (this.router.url === `/${url}`) {
      return 'active';
    }
    return '';
  }

  logout() {
    this.authenticationService.logout();
  }

  clearErrors() {
    this.alertService.clear();
  }
}
