import {Component, Injectable, OnInit} from '@angular/core';
import {Lecture} from '../../common/lecture';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
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
  option: string;
  submitted = false;
  searching = false;
  searchFailed = false;
  time: string;

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
    this.auditoriumModel = this.lecture.auditorium;
    this.groupModel = this.lecture.group;
    this.subjectModel = this.lecture.subject;
  }

  clearErrors() {
    this.alertService.clear();
  }

  createLecture(lectureDto) {
    this.lectureService.addLecture(lectureDto).subscribe(
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

    return {
      date: fullDate,
      auditoriumId: this.auditoriumModel.id,
      groupId: this.groupModel.id,
      lecturerId: this.lecturer.id,
      subjectId: this.subjectModel.id
    }
  }

  get form() { return this.lectureForm.controls; }

  initLectureForm() {
    const fullName = this.lecturer.lastName + ' ' + this.lecturer.firstName + ' ' + this.lecturer.patronymic;

    this.lectureForm = this.fb.group({
      date: new FormControl(this.lecture.date, Validators.required),
      time: new FormControl(this.time, Validators.required),
      auditorium: new FormControl(this.lecture.auditorium?.name, Validators.required),
      group: new FormControl(this.lecture.group?.name, Validators.required),
      lecturer: new FormControl({value:fullName, disabled:true}, Validators.required),
      subject: new FormControl({value:this.lecture.subject?.name, disabled: this.option !== 'create'}, Validators.required)
    });
  }

  refuse() {
    const lecturerId = this.lecturer.id;
    this.lecturer.id = null;

    this.save();

    this.lecturer.id = lecturerId;
  }

  save() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.lectureForm.invalid) {
      return;
    }

    this.loading = true;
    const lectureDto = this.fillModel();

    switch(this.option) {
      case 'create': {
        this.createLecture(lectureDto);
        break;
      }
      case 'update': {
        this.updateLecture(lectureDto);
        break;
      }
    }
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

  searchSubjects = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.searching = true),
      switchMap(term =>
        this.lectureService.getSubjects(term).pipe(
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
