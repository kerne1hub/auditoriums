import {Component, OnInit} from '@angular/core';
import {Subject} from '../../../common/subject';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {AlertService} from '../../../services/alert.service';
import {SubjectService} from '../../../services/subject.service';

@Component({
  selector: 'app-subject-form',
  templateUrl: './subject-form.component.html',
  styleUrls: ['./subject-form.component.css']
})
export class SubjectFormComponent implements OnInit {

  subject: Subject;
  subjectForm: FormGroup;
  loading = false;
  option: string;
  submitted = false;

  constructor(private fb: FormBuilder,
              private alertService: AlertService,
              private subjectService: SubjectService) { }

  ngOnInit(): void {
    this.initSubjectForm();
  }

  createSubject(subject: Subject) {
    this.subjectService.addSubject(subject).subscribe(
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

  fillModel(): Subject {
    return new Subject().build(this.form.name.value);
  }

  initSubjectForm() {
    this.subjectForm = this.fb.group({
      name: new FormControl(this.subject.name, Validators.required)
    });
  }

  get form() { return this.subjectForm.controls; }

  save() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.subjectForm.invalid) {
      return;
    }

    this.loading = true;
    const subject = this.fillModel();

    switch(this.option) {
      case 'create': {
        this.createSubject(subject);
        break;
      }
      case 'update': {
        this.updateSubject(subject);
        break;
      }
    }
  }

  updateSubject(subject: Subject) {
    this.subjectService.editSubject(this.subject.id, subject).subscribe(
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
