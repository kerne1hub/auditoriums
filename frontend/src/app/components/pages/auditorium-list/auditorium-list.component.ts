import {Component, OnInit} from '@angular/core';
import {Auditorium} from '../../../common/auditorium';
import {AuditoriumService} from '../../../services/auditorium.service';
import {BuildingService} from '../../../services/building.service';
import {Building} from '../../../common/building';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {Lecture} from '../../../common/lecture';

@Component({
  selector: 'app-auditorium-list',
  templateUrl: './auditorium-list.component.html',
  styleUrls: ['./auditorium-list.component.css']
})
export class AuditoriumListComponent implements OnInit {

  auditoriums: Auditorium[];
  buildingName = 'Здание';
  buildings: Building[];
  calendarDate: Date;
  congestion = 0;
  currentBuildingId = 1;
  days: string[] = [
    'Понедельник', 'Вторник', 'Среда', 'Четверг', 'Пятница', 'Суббота'
  ];
  date: NgbDateStruct;
  progressStyle = 'success';

  constructor(private auditoriumService: AuditoriumService,
              private buildingService: BuildingService) { }

  ngOnInit(): void {
    this.listBuildings();
    this.calendarDate = new Date();
    this.fillDate();
    this.getSchedule(this.currentBuildingId);
  }

  private setCalendar() {
    const dayOfWeek = this.calendarDate.getDay();
    this.calendarDate.setDate(dayOfWeek !== 0 ? this.calendarDate.getDate() - (dayOfWeek - 1) : this.calendarDate.getDate() - 6);
  }

  getSchedule(buildingId: number) {
    this.currentBuildingId = buildingId;
    this.calendarDate = this.date == null ? new Date() : new Date(this.date.year, this.date.month - 1, this.date.day);

    this.auditoriumService.getAuditoriumsWithLectures(buildingId, this.calendarDate).subscribe(
      data => {
        this.auditoriums = data;
        this.deserializeContent(data);
      }
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

  private fillDate() {
    this.date = {
      year: this.calendarDate.getFullYear(),
      month: this.calendarDate.getMonth() + 1,
      day: this.calendarDate.getDate()
    }
  }

  getDayOfWeek(day: number): Date {
    const date = new Date(this.calendarDate);
    date.setDate(this.calendarDate.getDate() + (day - 1));
    return date;
  }

  setPrevWeek() {
    this.setWeek(true);
    this.getSchedule(this.currentBuildingId);
  }

  setNextWeek() {
    this.setWeek();
    this.getSchedule(this.currentBuildingId);
  }

  private setWeek(inverse = false) {
    const currentDate = new Date(this.date.year, this.date.month  - 1, this.date.day);
    currentDate.setDate(inverse? this.date.day - 7 : this.date.day + 7);
    this.date = { year: currentDate.getFullYear(), month: currentDate.getMonth() + 1, day: currentDate.getDate() }
  }

  private deserializeContent(data: Auditorium[]) {
    const lectureMap = new Map();
    const subjectMap = new Map();
    const groupMap = new Map();
    let lectureCount = 0;

    data.forEach(a => {
      a.lectures.forEach(l => {
        if (l.lecturer && typeof l.lecturer === 'object') {
          lectureMap.set(l.lecturer.id, l.lecturer);
        } else if (typeof l.lecturer === 'number') {
          l.lecturer = lectureMap.get(l.lecturer);
        }

        lectureCount++;

        if (typeof l.subject === 'object') {
          subjectMap.set(l.subject.id, l.subject);
        } else if (typeof l.subject === 'number') {
          l.subject = subjectMap.get(l.subject);
        }

        if (typeof l.group === 'object') {
          groupMap.set(l.group.id, l.group);
        } else if (typeof l.group === 'number') {
          l.group = groupMap.get(l.group);
        }
      });
    });

    this.setCongestion(lectureCount / (data.length*36));
  }

  setCongestion(congestion: number) {
    this.congestion = congestion * 100;

    if (this.congestion > 50) {
      this.progressStyle = 'warning';
    } else if (this.congestion > 90) {
      this.progressStyle = 'danger';
    } else {
      this.progressStyle = 'success';
    }
  }
}
