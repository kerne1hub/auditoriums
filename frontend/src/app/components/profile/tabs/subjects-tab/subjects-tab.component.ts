import {Component, ComponentFactory, ComponentFactoryResolver, ComponentRef, OnInit, ViewContainerRef} from '@angular/core';
import {AlertService} from '../../../../services/alert.service';
import {SubjectService} from '../../../../services/subject.service';
import {Subject} from '../../../../common/subject';
import {SubjectFormComponent} from '../../../forms/subject-form/subject-form.component';

@Component({
  selector: 'app-subjects-tab',
  templateUrl: './subjects-tab.component.html',
  styleUrls: ['./subjects-tab.component.css']
})
export class SubjectsTabComponent implements OnInit {

  subjects: Subject[];

  private subjectFormRef: ComponentRef<SubjectFormComponent>;

  constructor(private alertService: AlertService,
              private subjectService: SubjectService,
              private resolver: ComponentFactoryResolver,
              private viewContainerRef: ViewContainerRef) { }

  ngOnInit(): void {
    this.getSubjects();
  }

  clearErrors() {
    this.alertService.clear();
  }

  createSubjectForm(instance = null) {
    if (this.subjectFormRef) {
      this.destroyComponent(this.subjectFormRef)
    }
    this.createComponent(instance);
  }

  createComponent(instance = null) {
    const componentFactory: ComponentFactory<SubjectFormComponent> =
      this.resolver.resolveComponentFactory(SubjectFormComponent);

    this.subjectFormRef = this.viewContainerRef.createComponent(componentFactory);
    const componentInstance = this.subjectFormRef.instance;

    componentInstance.subject = instance?? new Subject();
    componentInstance.option = instance? 'update': 'create';
  }

  deleteSubject(id: number) {
    this.subjectService.deleteSubject(id).subscribe(
      () => location.reload()
    );
  }

  private destroyComponent(componentRef: ComponentRef<any>) {
    componentRef.destroy();
    componentRef = null;
  }

  getSubjects() {
    this.subjectService.getSubjects().subscribe(
      data => this.subjects = data
    );
  }

}
