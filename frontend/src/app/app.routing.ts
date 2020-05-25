import { Routes, RouterModule } from '@angular/router';

import { AuditoriumListComponent } from './components/auditorium-list/auditorium-list.component';
import { LectureListComponent } from './components/lecture-list/lecture-list.component';
import { LoginFormComponent } from './components/login-form/login-form.component';

const routes: Routes = [
  { path: 'auditoriums', component: AuditoriumListComponent },
  { path: 'lectures', component: LectureListComponent },
  { path: '', redirectTo: '/auditoriums', pathMatch: 'full' },
  { path: 'login', component: LoginFormComponent },

  // otherwise redirect to home
  { path: '**', redirectTo: '' }
];

export const appRoutingModule = RouterModule.forRoot(routes);
