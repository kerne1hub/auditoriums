import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Building} from '../../../common/building';
import {AlertService} from '../../../services/alert.service';
import {BuildingService} from '../../../services/building.service';

@Component({
  selector: 'app-building-form',
  templateUrl: './building-form.component.html',
  styleUrls: ['./building-form.component.css']
})
export class BuildingFormComponent implements OnInit {

  building: Building;
  buildingForm: FormGroup;
  loading = false;
  option: string;
  submitted = false;

  constructor(private fb: FormBuilder,
              private alertService: AlertService,
              private buildingService: BuildingService) { }

  ngOnInit(): void {
    this.initBuildingForm();
  }

  createBuilding(building: Building) {
    this.buildingService.addBuilding(building).subscribe(
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

  fillModel(): Building {
    return new Building().build(this.form.name.value, this.form.address.value);
  }

  initBuildingForm() {
    this.buildingForm = this.fb.group({
      name: new FormControl(this.building.name, Validators.required),
      address: new FormControl(this.building.address, Validators.required)
    });
  }

  get form() { return this.buildingForm.controls; }

  save() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.buildingForm.invalid) {
      return;
    }

    this.loading = true;
    const building = this.fillModel();

    switch(this.option) {
      case 'create': {
        this.createBuilding(building);
        break;
      }
      case 'update': {
        this.updateBuilding(building);
        break;
      }
    }
  }

  updateBuilding(building: Building) {
    this.buildingService.editBuilding(this.building.id, building).subscribe(
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
