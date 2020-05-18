import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { AuditoriumListComponent } from './components/auditorium-list/auditorium-list.component';
import { AuditoriumService } from './services/auditorium.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { LecturesViewComponent } from './components/lectures-view/lectures-view.component';

@NgModule({
  declarations: [
    AppComponent,
    AuditoriumListComponent,
    LecturesViewComponent
  ],
    imports: [
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
