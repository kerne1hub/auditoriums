import {RouterModule, Routes} from '@angular/router';

import {AuditoriumListComponent} from './components/auditorium-list/auditorium-list.component';
import {LectureListComponent} from './components/lecture-list/lecture-list.component';
import {LoginFormComponent} from './components/login-form/login-form.component';
import {RegistrationFormComponent} from './components/registration-form/registration-form.component';
import {AuthGuard} from './helpers/auth.guard';
import {ProfileComponent} from './components/profile/profile.component';

const routes: Routes = [
  { path: 'auditoriums', component: AuditoriumListComponent },
  { path: 'lectures', component: LectureListComponent },
  { path: '', redirectTo: '/auditoriums', pathMatch: 'full' },
  // { path: 'login', component: LoginFormComponent },
  { path: 'register', component: RegistrationFormComponent },
  { path: 'me', component: ProfileComponent, canActivate: [AuthGuard] },

  // otherwise redirect to home
  { path: '**', redirectTo: '/auditoriums' }
];

export const appRoutingModule = RouterModule.forRoot(routes);
