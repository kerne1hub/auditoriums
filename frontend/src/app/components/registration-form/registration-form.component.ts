import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AlertService } from '../../services/alert.service';
import { AuthenticationService } from '../../services/authentication.service';
import { Lecturer } from '../../common/lecturer';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-registration-form',
  templateUrl: './registration-form.component.html',
  styleUrls: ['./registration-form.component.css']
})
export class RegistrationFormComponent implements OnInit {

  isLecturerCollapsed = true;
  registrationForm: FormGroup;
  loading = false;
  returnUrl: string;
  submitted = false;
  selectedType: string;
  userTypes = [{
    type: 'LECTURER', name: 'Преподаватель'
  }];

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private alertService: AlertService,
              private authService: AuthenticationService) { }

  ngOnInit(): void {
    this.registrationForm = this.formBuilder.group({
      login: ['', Validators.required],
      email: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      patronymic: '',
      userType: '',
      position: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    });

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  allCollapse() {
    this.isLecturerCollapsed = true;
  }

  // convenience getter for easy access to form fields
  get form() { return this.registrationForm.controls; }

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

  register() {
    console.log('clicked');
    switch (this.selectedType) {
      case 'LECTURER': {
        this.registerLecturer();
        break;
      }
    }
  }

  registerLecturer() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.registrationForm.invalid) {
      return;
    }

    const lecturer = new Lecturer(
      this.form.firstName.value,
      this.form.lastName.value,
      this.form.patronymic.value,
      this.form.login.value,
      this.form.password.value,
      this.form.email.value,
      this.form.position.value)

    this.loading = true;
    this.authService.registerLecturer(lecturer)
      .subscribe(
        data => {
          this.loading = false;
          // this.router.navigate(['/']);
          this.autoLogin(lecturer.login, lecturer.password);
        },
        error => {
          console.log(error);
          this.alertService.error(error);
          this.loading = false;
        });
  }

  autoLogin(login: string, password: string) {
    this.loading = true;
    this.authService.login(login, password).subscribe(
      data => {
        this.router.navigate(['/']);
    },
      error => {
        console.log(error);
        this.alertService.error(error);
        this.loading = false;
      })
  }

}
