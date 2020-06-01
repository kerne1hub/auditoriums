import {Component, ComponentFactory, ComponentFactoryResolver, ComponentRef, OnInit, ViewContainerRef} from '@angular/core';
import {Building} from '../../../common/building';
import {AlertService} from '../../../services/alert.service';
import {BuildingService} from '../../../services/building.service';
import {BuildingFormComponent} from '../../building-form/building-form.component';

@Component({
  selector: 'app-buildings-tab',
  templateUrl: './buildings-tab.component.html',
  styleUrls: ['./buildings-tab.component.css']
})
export class BuildingsTabComponent implements OnInit {

  buildings: Building[];

  private buildingFormRef: ComponentRef<BuildingFormComponent>;

  constructor(private alertService: AlertService,
              private buildingService: BuildingService,
              private resolver: ComponentFactoryResolver,
              private viewContainerRef: ViewContainerRef) { }

  ngOnInit(): void {
    this.getBuildings();
  }

  clearErrors() {
    this.alertService.clear();
  }

  createBuildingForm(instance = null) {
    if (this.buildingFormRef) {
      this.destroyComponent(this.buildingFormRef)
    }
    this.createComponent(instance);
  }

  createComponent(instance = null) {
    const componentFactory: ComponentFactory<BuildingFormComponent> =
      this.resolver.resolveComponentFactory(BuildingFormComponent);

    this.buildingFormRef = this.viewContainerRef.createComponent(componentFactory);
    const componentInstance = this.buildingFormRef.instance;

    componentInstance.building = instance?? new Building();
    componentInstance.option = instance? 'update': 'create';
  }

  deleteBuilding(id: number) {
    this.buildingService.deleteBuilding(id).subscribe(
      () => location.reload()
    );
  }

  private destroyComponent(componentRef: ComponentRef<any>) {
    componentRef.destroy();
    componentRef = null;
  }

  getBuildings() {
    this.buildingService.getBuildings().subscribe(
      data => this.buildings = data
    );
  }

}
