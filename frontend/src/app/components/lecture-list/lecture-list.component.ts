import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Lecture } from '../../common/lecture';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { LectureService } from '../../services/lecture.service';
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, switchMap, tap } from 'rxjs/operators';
import { Group } from '../../common/group';

@Component({
  selector: 'app-lecture-list',
  templateUrl: './lecture-list.component.html',
  styleUrls: ['./lecture-list.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class LectureListComponent implements OnInit {

  lectureMap = new Map<number, Lecture[]>();
  date: NgbDateStruct;
  calendarDate: Date;
  currentGroupId = 1;
  groupModel: Group;
  searching = false;
  searchFailed = false;
  formatter = (group: { name: string }) => group.name;

  constructor(private lectureService: LectureService) { }

  ngOnInit(): void {
    this.calendarDate = new Date();
    this.fillDate();
    this.getSchedule(this.currentGroupId);
  }

  searchGroup = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.searching = true),
      switchMap(term =>
        this.lectureService.getGroups(term).pipe(
          tap(() => this.searchFailed = false),
          catchError(() => {
            this.searchFailed = true;
            return of([]);
          }))
      ),
      tap(() => this.searching = false)
    )

  getSchedule(groupId: number) {
    this.currentGroupId = groupId;
    this.calendarDate = this.date == null ? new Date() : new Date(this.date.year, this.date.month - 1, this.date.day);

    this.lectureService.getLectures(groupId, this.calendarDate).subscribe(
      data => {
        this.deserializeContent(data);
        this.getLecturesByDay(data);
      }
    );
    this.setCalendar();
  }

  // month values in 0..11
  private fillDate() {
    this.date = {
      year: this.calendarDate.getFullYear(),
      month: this.calendarDate.getMonth() + 1,
      day: this.calendarDate.getDate()
    }
  }

  private getLecturesByDay(data: Lecture[]) {
    this.lectureMap.clear();

    for (let i = 1; i < 7; i++) {
      const lectures = data.filter(l => new Date(l.date).getDay() === i);
      if (lectures.length > 0) {
        this.lectureMap.set(i, lectures);
      }
    }
  }

  getDayOfWeek(day: number): Date {
    const date = new Date(this.calendarDate);
    date.setDate(this.calendarDate.getDate() + (day - 1));
    return date;
  }

  setPrevWeek() {
    this.setWeek(true);
    this.getSchedule(this.currentGroupId);
  }

  setNextWeek() {
    this.setWeek();
    this.getSchedule(this.currentGroupId);
  }

  private setWeek(inverse = false) {
    const currentDate = new Date(this.date.year, this.date.month, this.date.day);
    currentDate.setDate(inverse? this.date.day - 7 : this.date.day + 7);
    this.date = { year: currentDate.getFullYear(), month: currentDate.getMonth(), day: currentDate.getDate() }
  }

  selectGroup(group: Group) {
    this.getSchedule(group.id);
  }

  private setCalendar() {
    const dayOfWeek = this.calendarDate.getDay();
    this.calendarDate.setDate(dayOfWeek !== 0 ? this.calendarDate.getDate() - (dayOfWeek - 1) : this.calendarDate.getDate() - 6);
  }

  private deserializeContent(data: Lecture[]) {
    const auditoriumMap = new Map();
    const lecturerMap = new Map();
    const subjectMap = new Map();
    const groupMap = new Map();

    data.forEach(l => {
      if (typeof l.auditorium === 'object') {
        auditoriumMap.set(l.auditorium.id, l.auditorium);
      } else if (typeof l.auditorium === 'number') {
        l.auditorium = auditoriumMap.get(l.auditorium);
      }

      if (l.lecturer && typeof l.lecturer === 'object') {
        lecturerMap.set(l.lecturer.id, l.lecturer);
      } else if (typeof l.lecturer === 'number') {
        l.lecturer = lecturerMap.get(l.lecturer);
      }

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
  }
}
