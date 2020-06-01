import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {AlertService} from '../../../services/alert.service';
import {Group} from '../../../common/group';
import {GroupService} from '../../../services/group.service';

@Component({
  selector: 'app-group-form',
  templateUrl: './group-form.component.html',
  styleUrls: ['./group-form.component.css']
})
export class GroupFormComponent implements OnInit {

  group: Group;
  groupForm: FormGroup;
  loading = false;
  option: string;
  submitted = false;

  constructor(private fb: FormBuilder,
              private alertService: AlertService,
              private groupService: GroupService) { }

  ngOnInit(): void {
    this.initGroupForm();
  }

  createGroup(group: Group) {
    this.groupService.addGroup(group).subscribe(
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

  fillModel(): Group {
    return new Group().build(this.form.name.value);
  }

  initGroupForm() {
    this.groupForm = this.fb.group({
      name: new FormControl(this.group.name, Validators.required)
    });
  }

  get form() { return this.groupForm.controls; }

  save() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.groupForm.invalid) {
      return;
    }

    this.loading = true;
    const group = this.fillModel();

    switch(this.option) {
      case 'create': {
        this.createGroup(group);
        break;
      }
      case 'update': {
        this.updateGroup(group);
        break;
      }
    }
  }

  updateGroup(group: Group) {
    this.groupService.editGroup(this.group.id, group).subscribe(
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
