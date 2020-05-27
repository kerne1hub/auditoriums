import {Component, ComponentFactory, ComponentFactoryResolver, ComponentRef, Input, OnInit, ViewContainerRef} from '@angular/core';
import {LecturerService} from '../../services/lecturer.service';
import {Lecturer} from '../../common/lecturer';
import {AlertService} from '../../services/alert.service';
import {UserFormComponent} from '../user-form/user-form.component';
import {LectureFormComponent} from '../lecture-form/lecture-form.component';

@Component({
  selector: 'app-lecturer-page',
  templateUrl: './lecturer-page.component.html',
  styleUrls: ['./lecturer-page.component.css']
})
export class LecturerPageComponent implements OnInit {

  @Input() userId: number;
  lecturer: Lecturer;
  private userFormRef: ComponentRef<UserFormComponent>;
  private lectureFormRef: ComponentRef<LectureFormComponent>;

  constructor(
    private alertService: AlertService,
    private lecturerService: LecturerService,
    private resolver: ComponentFactoryResolver,
    private viewContainerRef: ViewContainerRef) { }

  ngOnInit(): void {
    this.getLecturer();
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

  getLecturer() {
    this.lecturerService.getLecturer(this.userId).subscribe(
      data => this.lecturer = data
    );
  }

  private destroyComponent(componentRef: ComponentRef<any>) {
    componentRef.destroy();
    componentRef = null;
  }

}
