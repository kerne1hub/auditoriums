import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AlertService} from '../../services/alert.service';
import {LecturerService} from '../../services/lecturer.service';
import {Lecturer} from '../../common/lecturer';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit {

  @Input() lecturer: Lecturer;
  isLecturerCollapsed = true;
  userForm: FormGroup;
  loading = false;
  submitted = false;
  selectedType: string;
  userTypes = [{
    type: 'LECTURER', name: 'Преподаватель'
  }];

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private lecturerService: LecturerService,
              private alertService: AlertService) { }

  ngOnInit(): void {
    this.userForm = this.formBuilder.group({
      login: [this.lecturer.login, Validators.required],
      email: [this.lecturer.email, Validators.required],
      firstName: [this.lecturer.firstName, Validators.required],
      lastName: [this.lecturer.lastName, Validators.required],
      patronymic: this.lecturer.patronymic,
      userType: this.userTypes.find(s => s.type === this.lecturer.userType).name,
      position: [this.lecturer.position, Validators.required],
      password: ['', Validators.required]
    });
  }

  allCollapse() {
    this.isLecturerCollapsed = true;
  }

  clearErrors() {
    this.alertService.clear();
  }

  fillModel(): Lecturer {
    return new Lecturer(
      this.lecturer.id,
      this.form.firstName.value,
      this.form.lastName.value,
      this.form.patronymic.value,
      this.form.login.value,
      this.form.password.value,
      this.form.email.value,
      this.form.position.value
    )
  }

  get form() { return this.userForm.controls; }

  update() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.userForm.invalid) {
      return;
    }

    this.loading = true;
    const lecturer = this.fillModel();

    this.lecturerService.editLecturer(lecturer)
      .subscribe(
        data => {
          this.loading = false;
          this.lecturer = data;
          this.router.navigate(['/']);
        },
        error => {
          console.log(error);
          this.alertService.error(error);
          this.loading = false;
        });
  }

  selectType(userType) {
    switch (userType.type) {
      case 'LECTURER': {
        this.isLecturerCollapsed = false;
        break;
      }
      default: {
        this.allCollapse();
      }
    }

    this.selectedType = userType.type
  }
}
