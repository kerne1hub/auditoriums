import {Component, ComponentFactory, ComponentFactoryResolver, ComponentRef, OnInit, ViewChild, ViewContainerRef} from '@angular/core';
import { Auditorium } from '../../common/auditorium';
import { AuditoriumService } from '../../services/auditorium.service';
import { BuildingService } from '../../services/building.service';
import { Building } from '../../common/building';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import {Lecture} from '../../common/lecture';
import {LecturesViewComponent} from '../lectures-view/lectures-view.component';

@Component({
  selector: 'app-auditorium-list',
  templateUrl: './auditorium-list.component.html',
  styleUrls: ['./auditorium-list.component.css']
})
export class AuditoriumListComponent implements OnInit {
  buildings: Building[];
  auditoriums: Auditorium[];
  date: NgbDateStruct;
  calendarDate: Date;
  currentBuildingId = 1;
  days: string[] = [
    'Понедельник', 'Вторник', 'Среда', 'Четверг', 'Пятница', 'Суббота'
  ];

  constructor(private auditoriumService: AuditoriumService,
              private buildingService: BuildingService) { }

  ngOnInit(): void {
    this.listBuildings();
    this.calendarDate = new Date();
  }

  private setCalendar() {
    const dayOfWeek = this.calendarDate.getDay();
    this.calendarDate.setDate(dayOfWeek !== 0 ? this.calendarDate.getDate() - (dayOfWeek - 1) : this.calendarDate.getDate() - 6);
  }

  getSchedule(buildingId: number) {
    this.currentBuildingId = buildingId;
    this.calendarDate = this.date == null ? new Date() : new Date(this.date.year, this.date.month - 1, this.date.day);

    this.auditoriumService.getAuditoriums(buildingId, this.calendarDate).subscribe(
      data => this.auditoriums = data
    );

    this.setCalendar();
  }

  listBuildings() {
    this.buildingService.getBuildings().subscribe(
      data => this.buildings = data
    );
  }

  getLecturesByDay(lectures: Lecture[], day: number): Lecture[] {
    return lectures.filter(l => new Date(l.date).getDay() === day);
  }

  getDayOfWeek(day: number): Date {
    const date = new Date(this.calendarDate);
    date.setDate(this.calendarDate.getDate() + (day - 1));
    return date;
  }

}
