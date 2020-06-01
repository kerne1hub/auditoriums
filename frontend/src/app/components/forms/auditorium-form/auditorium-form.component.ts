import { Component, OnInit } from '@angular/core';
import {Auditorium} from '../../../common/auditorium';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Building} from '../../../common/building';
import {AlertService} from '../../../services/alert.service';
import {AuditoriumService} from '../../../services/auditorium.service';
import {Observable, of} from 'rxjs';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators';
import {BuildingService} from '../../../services/building.service';

@Component({
  selector: 'app-auditorium-form',
  templateUrl: './auditorium-form.component.html',
  styleUrls: ['./auditorium-form.component.css']
})
export class AuditoriumFormComponent implements OnInit {

  auditorium: Auditorium;
  auditoriumForm: FormGroup;
  loading = false;
  option: string;
  submitted = false;
  searching = false;
  searchFailed = false;

  buildingModel: Building;

  formatter = (model: { name: string }) => model.name;

  constructor(private fb: FormBuilder,
              private alertService: AlertService,
              private buildingService: BuildingService,
              private auditoriumService: AuditoriumService) { }

  ngOnInit(): void {
    this.buildingModel = this.auditorium.building;
    this.initAuditoriumForm();
  }

  createAuditorium(auditoriumDto) {
    this.auditoriumService.addAuditorium(auditoriumDto).subscribe(
      () => {
        this.loading = false;
        location.reload();
      },
      error => {
        console.log(error);
        this.alertService.error(error);
        this.loading = false;
      });
  }

  fillModel(): any {
    return {
      buildingId: this.buildingModel.id,
      name: this.form.name.value,
      capacity: this.form.capacity.value,
      active: this.form.active.value
    }
  }

  initAuditoriumForm() {
    this.auditoriumForm = this.fb.group({
      name: new FormControl(this.auditorium.name, Validators.required),
      capacity: new FormControl(this.auditorium.capacity, Validators.required),
      building: new FormControl(this.buildingModel, Validators.required),
      active: new FormControl(this.auditorium.active)
    });
  }

  get form() { return this.auditoriumForm.controls; }

  searchBuildings = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.searching = true),
      switchMap(term =>
        this.buildingService.getBuildingsByName(term).pipe(
          tap(() => this.searchFailed = false),
          catchError(() => {
            this.searchFailed = true;
            return of([]);
          }))
      ),
      tap(() => this.searching = false)
    );

  selectModel(model): number {
    return model.id;
  }

  save() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.auditoriumForm.invalid) {
      return;
    }

    this.loading = true;
    const lectureDto = this.fillModel();

    switch(this.option) {
      case 'create': {
        this.createAuditorium(lectureDto);
        break;
      }
      case 'update': {
        this.updateAuditorium(lectureDto);
        break;
      }
    }
  }

  updateAuditorium(auditoriumDto) {
    this.auditoriumService.editAuditorium(this.auditorium.id, auditoriumDto).subscribe(
      () => {
        this.loading = false;
        location.reload();
      },
      error => {
        console.log(error);
        this.alertService.error(error);
        this.loading = false;
      });
  }

}
