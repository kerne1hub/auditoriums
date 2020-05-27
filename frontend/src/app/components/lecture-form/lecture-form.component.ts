import {Component, Injectable, OnInit} from '@angular/core';
import {Lecture} from '../../common/lecture';
import {FormBuilder, FormGroup} from '@angular/forms';
import {AlertService} from '../../services/alert.service';
import {LectureService} from '../../services/lecture.service';
import {Lecturer} from '../../common/lecturer';
import {Auditorium} from '../../common/auditorium';
import {Subject} from '../../common/subject';
import {Group} from '../../common/group';
import {NgbDateStruct, NgbTimeAdapter, NgbTimeStruct} from '@ng-bootstrap/ng-bootstrap';
import {Observable, of} from 'rxjs';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators';
import {AuditoriumService} from '../../services/auditorium.service';

const pad = (i: number): string => i < 10 ? `0${i}` : `${i}`;

/**
 * String Time adapter
 */
@Injectable()
export class NgbTimeStringAdapter extends NgbTimeAdapter<string> {

  fromModel(value: string| null): NgbTimeStruct | null {
    console.log(value);
    if (!value) {
      return null;
    }
    const split = value.split(':');
    return {
      hour: parseInt(split[0], 10),
      minute: parseInt(split[1], 10),
      second: parseInt(split[2], 10)
    };
  }

  toModel(time: NgbTimeStruct | null): string | null {
    return time != null ? `${pad(time.hour)}:${pad(time.minute)}` : null;
  }
}

@Component({
  selector: 'app-lecture-form',
  templateUrl: './lecture-form.component.html',
  styleUrls: ['./lecture-form.component.css'],
  providers: [{provide: NgbTimeAdapter, useClass: NgbTimeStringAdapter}]
})
export class LectureFormComponent implements OnInit {

  date: NgbDateStruct;
  lecture: Lecture;
  lecturer: Lecturer;
  lectureForm: FormGroup;
  loading = false;
  time: string;
  searching = false;
  searchFailed = false;

  auditoriumModel: Auditorium;
  groupModel: Group;
  subjectModel: Subject;

  formatter = (model: { name: string }) => model.name;

  constructor(private fb: FormBuilder,
              private alertService: AlertService,
              private auditoriumService: AuditoriumService,
              private lectureService: LectureService) { }

  ngOnInit(): void {
    this.initLectureForm();
    this.time = new Date(this.lecture.date).toLocaleTimeString();
    this.fillDate();
    this.auditoriumModel = (this.lecture as Lecture).auditorium as Auditorium;
    this.groupModel = (this.lecture as Lecture).group as Group;
    this.subjectModel = (this.lecture as Lecture).subject as Subject;
  }

  clearErrors() {
    this.alertService.clear();
  }

  private fillDate() {
    const date = new Date(this.lecture.date);

    this.date = {
      year: date.getFullYear(),
      month: date.getMonth() + 1,
      day: date.getDate()
    }
  }

  // "yyyy-MM-dd'T'HH:mm:ss"
  fillModel(): any {
    const fullDate = new Date(this.date.year, this.date.month - 1, this.date.day, +this.time.substr(0, 2), +this.time.substr(3, 2));
    fullDate.setHours(fullDate.getHours() + 6);
    console.log(fullDate.toString());
    return {
      date: fullDate,
      auditoriumId: this.auditoriumModel.id,
      groupId: this.groupModel.id,
      lecturerId: this.lecturer.id,
      subjectId: this.subjectModel.id
    }
  }

  initLectureForm() {
    const fullName = this.lecturer.lastName + ' ' + this.lecturer.firstName + ' ' + this.lecturer.patronymic;

    this.lectureForm = this.fb.group({
      date: this.lecture.date,
      time: this.time,
      auditorium: (this.lecture.auditorium as Auditorium).name,
      group: (this.lecture.group as Group).name,
      lecturer: fullName,
      subject: (this.lecture.subject as Subject).name
    });
  }

  save() {
    this.loading = true;
    const lectureDto = this.fillModel();

    this.updateLecture(lectureDto);
  }

  searchAuditoriums = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.searching = true),
      switchMap(term =>
        this.auditoriumService.getAuditoriumsByName(term).pipe(
          tap(() => this.searchFailed = false),
          catchError(() => {
            this.searchFailed = true;
            return of([]);
          }))
      ),
      tap(() => this.searching = false)
    );

  searchGroups = (text$: Observable<string>) =>
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

  selectModel(model): number {
    return model.id;
  }

  updateLecture(lectureDto) {
    this.lectureService.editLecture(this.lecture.id, lectureDto).subscribe(
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
