import {Component, ComponentFactory, ComponentFactoryResolver, ComponentRef, Input, OnInit, ViewContainerRef} from '@angular/core';
import {LecturerService} from '../../services/lecturer.service';
import {Lecturer} from '../../common/lecturer';
import {AlertService} from '../../services/alert.service';
import {UserFormComponent} from '../user-form/user-form.component';
import {LectureFormComponent} from '../lecture-form/lecture-form.component';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {LectureService} from '../../services/lecture.service';
import {Lecture} from '../../common/lecture';

@Component({
  selector: 'app-lecturer-page',
  templateUrl: './lecturer-page.component.html',
  styleUrls: ['./lecturer-page.component.css']
})
export class LecturerPageComponent implements OnInit {

  date: NgbDateStruct;
  @Input() userId: number;
  lecturer: Lecturer;
  lectures: Lecture[];
  undefinedLectures: Lecture[];
  private userFormRef: ComponentRef<UserFormComponent>;
  private lectureFormRef: ComponentRef<LectureFormComponent>;

  constructor(
    private alertService: AlertService,
    private lectureService: LectureService,
    private lecturerService: LecturerService,
    private resolver: ComponentFactoryResolver,
    private viewContainerRef: ViewContainerRef) { }

  ngOnInit(): void {
    this.fillDate();
    this.getLecturer();
    this.getLectures();
    this.getUndefinedLectures();
  }

  clearErrors() {
    this.alertService.clear();
  }

  createComponent(type, instance = null) {
    if (type === 'user') {
      const componentFactory: ComponentFactory<UserFormComponent> =
        this.resolver.resolveComponentFactory(UserFormComponent);

      this.userFormRef = this.viewContainerRef.createComponent(componentFactory);
      const componentInstance = this.userFormRef.instance;

      componentInstance.lecturer = instance;
    } else if (type === 'lecture') {
      const componentFactory: ComponentFactory<LectureFormComponent> =
        this.resolver.resolveComponentFactory(LectureFormComponent);

      this.lectureFormRef = this.viewContainerRef.createComponent(componentFactory);
      const componentInstance = this.lectureFormRef.instance;

      componentInstance.lecture = instance;
      componentInstance.lecturer = this.lecturer;
    }
  }

  createLectureForm(instance) {
    if (this.lectureFormRef) {
      this.destroyComponent(this.lectureFormRef)
      // this.updateComponent(instance, 'lecture', this.lectureFormRef);
      // } else {
    }
      this.createComponent('lecture', instance);
  }

  createUserForm(instance) {
    if (this.userFormRef) {
      this.destroyComponent(this.userFormRef)
    }
      this.createComponent('user', instance);
  }

  // month values in 0..11
  private fillDate() {
    const date = new Date();

    this.date = {
      year: date.getFullYear(),
      month: date.getMonth() + 1,
      day: date.getDate()
    }
  }

  getLecturer() {
    this.lecturerService.getLecturer(this.userId).subscribe(
      data => this.lecturer = data
    );
  }

  getLectures() {
    const date = new Date(this.date.year, this.date.month - 1, this.date.day);
    this.lectureService.getLecturesByLecturer(this.userId, date).subscribe(
      data => this.lectures = data
    );
  }

  getUndefinedLectures() {
    const date = new Date(this.date.year, this.date.month - 1, this.date.day);
    this.lectureService.getUndefinedLectures(date).subscribe(
      data => this.undefinedLectures = data
    );
  }

  private destroyComponent(componentRef: ComponentRef<any>) {
    componentRef.destroy();
    componentRef = null;
  }

  setPrevWeek() {
    this.setWeek(true);
    this.getLectures();
  }

  setNextWeek() {
    this.setWeek();
    this.getLectures();
  }

  private setWeek(inverse = false) {
    const currentDate = new Date(this.date.year, this.date.month, this.date.day);
    currentDate.setDate(inverse? this.date.day - 7 : this.date.day + 7);
    this.date = { year: currentDate.getFullYear(), month: currentDate.getMonth(), day: currentDate.getDate() }
  }

}
