import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { AuditoriumListComponent } from './components/auditorium-list/auditorium-list.component';
import { AuditoriumService } from './services/auditorium.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {FormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    AuditoriumListComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    NgbModule,
    FormsModule
  ],
  providers: [
    AuditoriumService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
