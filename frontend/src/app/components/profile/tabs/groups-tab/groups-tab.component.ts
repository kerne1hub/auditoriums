import {Component, ComponentFactory, ComponentFactoryResolver, ComponentRef, OnInit, ViewContainerRef} from '@angular/core';
import {Group} from '../../../../common/group';
import {AlertService} from '../../../../services/alert.service';
import {GroupFormComponent} from '../../../forms/group-form/group-form.component';
import {GroupService} from '../../../../services/group.service';

@Component({
  selector: 'app-groups-tab',
  templateUrl: './groups-tab.component.html',
  styleUrls: ['./groups-tab.component.css']
})
export class GroupsTabComponent implements OnInit {

  groups: Group[];

  private groupFormRef: ComponentRef<GroupFormComponent>;

  constructor(private alertService: AlertService,
              private groupService: GroupService,
              private resolver: ComponentFactoryResolver,
              private viewContainerRef: ViewContainerRef) { }

  ngOnInit(): void {
    this.getGroups();
  }

  clearErrors() {
    this.alertService.clear();
  }

  createGroupForm(instance = null) {
    if (this.groupFormRef) {
      this.destroyComponent(this.groupFormRef)
    }
    this.createComponent(instance);
  }

  createComponent(instance = null) {
    const componentFactory: ComponentFactory<GroupFormComponent> =
      this.resolver.resolveComponentFactory(GroupFormComponent);

    this.groupFormRef = this.viewContainerRef.createComponent(componentFactory);
    const componentInstance = this.groupFormRef.instance;

    componentInstance.group = instance?? new Group();
    componentInstance.option = instance? 'update': 'create';
  }

  deleteGroup(id: number) {
    this.groupService.deleteGroup(id).subscribe(
      () => location.reload()
    );
  }

  private destroyComponent(componentRef: ComponentRef<any>) {
    componentRef.destroy();
    componentRef = null;
  }

  getGroups() {
    this.groupService.getGroups().subscribe(
      data => this.groups = data
    );
  }

}
