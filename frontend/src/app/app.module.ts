import { BrowserModule } from '@angular/platform-browser';
import { LOCALE_ID, NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { appRoutingModule } from './app.routing';
import { AppComponent } from './app.component';
import { AuditoriumListComponent } from './components/pages/auditorium-list/auditorium-list.component';
import { AuditoriumService } from './services/auditorium.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LecturesViewComponent } from './components/lectures-view/lectures-view.component';
import { LectureListComponent } from './components/pages/lecture-list/lecture-list.component';
import { registerLocaleData } from '@angular/common';
import localeRu from '@angular/common/locales/ru';
import { JwtInterceptor } from './helpers/jwt.interceptor';
import { ErrorInterceptor } from './helpers/error.interceptor';
import { LoginFormComponent } from './components/forms/login-form/login-form.component';
import { AlertComponent } from './components/alert/alert.component';
import { RegistrationFormComponent } from './components/forms/registration-form/registration-form.component';
import { LecturerPageComponent } from './components/pages/lecturer-page/lecturer-page.component';
import { ProfileComponent } from './components/profile/profile.component';
import { UserFormComponent } from './components/forms/user-form/user-form.component';
import { LectureFormComponent } from './components/forms/lecture-form/lecture-form.component';
import { AdminPageComponent } from './components/pages/admin-page/admin-page.component';
import { AuditoriumsTabComponent } from './components/profile/tabs/auditoriums-tab/auditoriums-tab.component';
import { AuditoriumFormComponent } from './components/forms/auditorium-form/auditorium-form.component';
import { BuildingsTabComponent } from './components/profile/tabs/buildings-tab/buildings-tab.component';
import { BuildingFormComponent } from './components/forms/building-form/building-form.component';
import { GroupFormComponent } from './components/forms/group-form/group-form.component';
import { GroupsTabComponent } from './components/profile/tabs/groups-tab/groups-tab.component';
import { SubjectsTabComponent } from './components/profile/tabs/subjects-tab/subjects-tab.component';
import { SubjectFormComponent } from './components/forms/subject-form/subject-form.component';

registerLocaleData(localeRu, 'ru');

@NgModule({
  declarations: [
    AppComponent,
    AuditoriumListComponent,
    LecturesViewComponent,
    LectureListComponent,
    LoginFormComponent,
    AlertComponent,
    RegistrationFormComponent,
    LecturerPageComponent,
    ProfileComponent,
    UserFormComponent,
    LectureFormComponent,
    AdminPageComponent,
    AuditoriumsTabComponent,
    AuditoriumFormComponent,
    BuildingsTabComponent,
    BuildingFormComponent,
    GroupFormComponent,
    GroupsTabComponent,
    SubjectsTabComponent,
    SubjectFormComponent,
  ],
  imports: [
    appRoutingModule,
    BrowserModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    AuditoriumService,
    { provide: LOCALE_ID, useValue: 'ru' },
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
