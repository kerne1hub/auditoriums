import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AlertService} from '../../../services/alert.service';
import {LecturerService} from '../../../services/lecturer.service';
import {Lecturer} from '../../../common/lecturer';
import {User} from '../../../common/user';
import {AuthenticationService} from '../../../services/authentication.service';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit {

  @Input() user: User;
  isLecturerCollapsed = true;
  userForm: FormGroup;
  loading = false;
  submitted = false;
  selectedType;
  userTypes = [
    { type: 'ADMIN', name: 'Администратор' },
    { type: 'LECTURER', name: 'Преподаватель' },
    { type: 'USER', name: 'Пользователь' }];

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private authService: AuthenticationService,
              private lecturerService: LecturerService,
              private alertService: AlertService) { }

  ngOnInit(): void {
    this.selectedType = this.userTypes.find(s => s.type === this.user.userType);
    this.userForm = this.formBuilder.group({
      login: [this.user.login, Validators.required],
      email: [this.user.email, Validators.required],
      firstName: [this.user.firstName, Validators.required],
      lastName: [this.user.lastName, Validators.required],
      patronymic: this.user.patronymic,
      userType: this.selectedType.name,
      password: ['', Validators.required]
    });

    if (this.user.userType === 'LECTURER') {
      this.userForm.addControl('position', new FormControl((this.user as Lecturer).position, Validators.required));
    }
  }

  allCollapse() {
    this.isLecturerCollapsed = true;
  }

  clearErrors() {
    this.alertService.clear();
  }

  fillModel(): User {
    if (this.user.userType === 'LECTURER') {
      return new Lecturer().buildLecturer(
        this.user.id,
        this.form.firstName.value,
        this.form.lastName.value,
        this.form.patronymic.value,
        this.form.login.value,
        this.form.password.value,
        this.form.email.value,
        this.form.position.value
      )
    }
    return new User().buildUser(
      this.user.id,
      this.form.firstName.value,
      this.form.lastName.value,
      this.form.patronymic.value,
      this.form.login.value,
      this.form.password.value,
      this.selectedType.type,
      this.form.email.value)
  }

  get form() { return this.userForm.controls; }

  update() {
    this.submitted = true;

    this.alertService.clear();

    // stop here if form is invalid
    if (this.userForm.invalid) {
      return;
    }

    this.loading = true;
    const user = this.fillModel();

    if (user.userType === 'LECTURER') {
      this.updateLecturer(user as Lecturer);
      return;
    }

    this.updateUser(user);
  }

  updateLecturer(lecturer: Lecturer) {
    this.lecturerService.editLecturer(lecturer)
      .subscribe(
        data => {
          this.loading = false;
          this.user = data;
          localStorage.setItem('currentUser', JSON.stringify(data));
          location.reload();
        },
        error => {
          console.log(error);
          this.alertService.error(error);
          this.loading = false;
        });
  }

  updateUser(user: User) {
    this.authService.editProfile(user)
      .subscribe(
        data => {
          this.loading = false;
          this.user = data;
          localStorage.setItem('currentUser', JSON.stringify(data));
          location.reload();
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
      case 'USER': {
        this.isLecturerCollapsed = true;
        break;
      }
      default: {
        this.allCollapse();
      }
    }

    this.selectedType = userType
  }
}
