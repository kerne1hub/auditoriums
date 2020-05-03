import { Component, OnInit } from '@angular/core';
import { Auditorium } from '../../common/auditorium';
import { AuditoriumService } from '../../services/auditorium.service';
import { BuildingService } from '../../services/building.service';
import { Building } from '../../common/building';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import {Lecture} from '../../common/lecture';

@Component({
  selector: 'app-auditorium-list',
  templateUrl: './auditorium-list.component.html',
  styleUrls: ['./auditorium-list.component.css']
})
export class AuditoriumListComponent implements OnInit {

  buildings: Building[];
  auditoriums: Auditorium[];
  date: NgbDateStruct;
  currentBuildingId = 1;

  constructor(private auditoriumService: AuditoriumService,
              private buildingService: BuildingService) { }

  ngOnInit(): void {
    this.listBuildings();
  }

  getSchedule(buildingId: number) {
    this.currentBuildingId = buildingId;
    const date = this.date == null ? new Date() : new Date(this.date.year, this.date.month - 1, this.date.day + 1);

    this.auditoriumService.getAuditoriums(buildingId, date).subscribe(
      data => this.auditoriums = data
    );
  }

  listBuildings() {
    this.buildingService.getBuildings().subscribe(
      data => this.buildings = data
    );
  }

  getByDay(lectures: Lecture[], day: number): number {
    return lectures.filter(l => new Date(l.date).getDay() === day).length;
  }

}
