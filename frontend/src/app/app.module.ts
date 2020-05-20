import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { appRoutingModule } from './app.routing';
import { AppComponent } from './app.component';
import { AuditoriumListComponent } from './components/auditorium-list/auditorium-list.component';
import { AuditoriumService } from './services/auditorium.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LecturesViewComponent } from './components/lectures-view/lectures-view.component';
import { LectureListComponent } from './components/lecture-list/lecture-list.component';

@NgModule({
  declarations: [
    AppComponent,
    AuditoriumListComponent,
    LecturesViewComponent,
    LectureListComponent
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
    AuditoriumService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
