import { Component, OnInit } from '@angular/core';
import {Auditorium} from '../../../common/auditorium';
import {AuditoriumService} from '../../../services/auditorium.service';
import {BuildingService} from '../../../services/building.service';
import {Building} from '../../../common/building';

@Component({
  selector: 'app-auditoriums-tab',
  templateUrl: './auditoriums-tab.component.html',
  styleUrls: ['./auditoriums-tab.component.css']
})
export class AuditoriumsTabComponent implements OnInit {

  auditoriums: Auditorium[];
  buildings: Building[];
  currentBuildingId = 1;

  constructor(private auditoriumService: AuditoriumService,
              private buildingService: BuildingService) { }

  ngOnInit(): void {
    this.listBuildings();
    this.getAuditoriums(this.currentBuildingId);
  }

  getAuditoriums(buildingId: number) {
    this.currentBuildingId = buildingId;

    this.auditoriumService.getAuditoriumsByBuilding(buildingId).subscribe(
      data => {
        this.auditoriums = data;
      }
    );
  }

  listBuildings() {
    this.buildingService.getBuildings().subscribe(
      data => this.buildings = data
    );
  }

}
