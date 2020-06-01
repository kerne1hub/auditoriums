import {
  Component,
  ComponentFactory,
  ComponentFactoryResolver,
  ComponentRef,
  Input,
  OnDestroy,
  OnInit,
  ViewContainerRef
} from '@angular/core';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {Lecture} from '../../../common/lecture';
import {UserFormComponent} from '../../forms/user-form/user-form.component';
import {User} from '../../../common/user';
import {AlertService} from '../../../services/alert.service';
import {LectureService} from '../../../services/lecture.service';
import {LecturerService} from '../../../services/lecturer.service';
import {LectureFormComponent} from '../../forms/lecture-form/lecture-form.component';
import {AuthenticationService} from '../../../services/authentication.service';
import {Lecturer} from '../../../common/lecturer';
import {Building} from '../../../common/building';
import {BuildingService} from '../../../services/building.service';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit, OnDestroy {

  buildingName = 'Здание';
  buildings: Building[];
  currentBuildingId = 1;
  date: NgbDateStruct;
  @Input() userId: number;
  user: User;
  lectures: Lecture[];
  loading = false;
  undefinedLectures: Lecture[];

  private lectureFormRef: ComponentRef<LectureFormComponent>;
  private userFormRef: ComponentRef<UserFormComponent>;

  constructor(
    private alertService: AlertService,
    private authService: AuthenticationService,
    private buildingService: BuildingService,
    private lectureService: LectureService,
    private lecturerService: LecturerService,
    private resolver: ComponentFactoryResolver,
    private viewContainerRef: ViewContainerRef) {
  }

  ngOnDestroy(): void {
    this.lectureService.clearActiveTab();
  }

  ngOnInit(): void {
    this.fillDate();
    this.getBuildings();
    this.getLectures(this.currentBuildingId);
    this.getUndefinedLectures();
    this.getUser();
  }

  clearErrors() {
    this.alertService.clear();
  }

  // TODO: add search params to exclude undefined
  createComponent(type, instance = null) {
    if (type === 'user') {
      const componentFactory: ComponentFactory<UserFormComponent> =
        this.resolver.resolveComponentFactory(UserFormComponent);

      this.userFormRef = this.viewContainerRef.createComponent(componentFactory);
      const componentInstance = this.userFormRef.instance;

      componentInstance.user = instance;
    } else if (type === 'lecture') {
      const componentFactory: ComponentFactory<LectureFormComponent> =
        this.resolver.resolveComponentFactory(LectureFormComponent);

      this.lectureFormRef = this.viewContainerRef.createComponent(componentFactory);
      const componentInstance = this.lectureFormRef.instance;

      componentInstance.lecture = instance?? new Lecture();
      componentInstance.lecturerModel = instance?.lecturer?? new Lecturer();
      componentInstance.option = instance? 'update': 'create';
    }
  }

  createLectureForm(instance = null) {
    if (this.lectureFormRef) {
      this.destroyComponent(this.lectureFormRef)
    }
    this.createComponent('lecture', instance);
  }

  createUserForm(instance) {
    if (this.userFormRef) {
      this.destroyComponent(this.userFormRef)
    }
    this.createComponent('user', instance);
  }

  deleteLecture(id: number) {
    this.lectureService.deleteLecture(id).subscribe(
      () => location.reload()
    );
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

  // "yyyy-MM-dd'T'HH:mm:ss"
  fillModel(lecture: Lecture): any {
    return {
      date: lecture.date,
      auditoriumId: lecture.auditorium.id,
      groupId: lecture.group.id,
      lecturerId: lecture.lecturer.id,
      subjectId: lecture.subject.id
    }
  }

  getActiveTab() {
    return this.lectureService.getActiveTab();
  }

  getBuildings() {
    this.buildingService.getBuildings().subscribe(
      data => this.buildings = data
    );
  }

  setActiveTab(tab: string) {
    this.lectureService.setActiveTab(tab);
  }

  isActiveTab(tab: string) {
    if (this.getActiveTab() === tab) {
      return 'active';
    }
    return '';
  }

  getUser() {
    this.authService.currentUser.subscribe(
      data => this.user = data
    );
  }

  getLectures(buildingId: number) {
    this.currentBuildingId = buildingId;

    const date = new Date(this.date.year, this.date.month - 1, this.date.day);
    this.lectureService.getLecturesByBuilding(buildingId, date).subscribe(
      data => {
        this.deserializeContent(data);
        this.lectures = data;
      }
    );
  }

  getUndefinedLectures() {
    const date = new Date(this.date.year, this.date.month - 1, this.date.day);
    this.lectureService.getUndefinedLectures(date).subscribe(
      data => {
        this.deserializeContent(data);
        this.undefinedLectures = data
      }
    );
  }

  private destroyComponent(componentRef: ComponentRef<any>) {
    componentRef.destroy();
    componentRef = null;
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

  setPrevWeek() {
    this.setWeek(true);
    this.getLectures(this.currentBuildingId);
    this.getUndefinedLectures();
  }

  setNextWeek() {
    this.setWeek();
    this.getLectures(this.currentBuildingId);
    this.getUndefinedLectures();
  }

  private setWeek(inverse = false) {
    const currentDate = new Date(this.date.year, this.date.month  - 1, this.date.day);

    currentDate.setDate(inverse? this.date.day - 7 : this.date.day + 7);
    this.date = { year: currentDate.getFullYear(), month: currentDate.getMonth() + 1, day: currentDate.getDate() }
  }

}
