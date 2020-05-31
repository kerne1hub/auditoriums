import {Component, ComponentFactory, ComponentFactoryResolver, ComponentRef, OnInit, ViewContainerRef} from '@angular/core';
import {Auditorium} from '../../../common/auditorium';
import {AuditoriumService} from '../../../services/auditorium.service';
import {BuildingService} from '../../../services/building.service';
import {Building} from '../../../common/building';
import {AuditoriumFormComponent} from '../../auditorium-form/auditorium-form.component';
import {AlertService} from '../../../services/alert.service';

@Component({
  selector: 'app-auditoriums-tab',
  templateUrl: './auditoriums-tab.component.html',
  styleUrls: ['./auditoriums-tab.component.css']
})
export class AuditoriumsTabComponent implements OnInit {

  auditoriums: Auditorium[];
  buildings: Building[];
  currentBuildingId = 1;
  private auditoriumFormRef: ComponentRef<AuditoriumFormComponent>;

  constructor(private alertService: AlertService,
              private auditoriumService: AuditoriumService,
              private buildingService: BuildingService,
              private resolver: ComponentFactoryResolver,
              private viewContainerRef: ViewContainerRef) { }

  ngOnInit(): void {
    this.listBuildings();
    this.getAuditoriums(this.currentBuildingId);
  }

  clearErrors() {
    this.alertService.clear();
  }

  createAuditoriumForm(instance = null) {
    if (this.auditoriumFormRef) {
      this.destroyComponent(this.auditoriumFormRef)
    }
    this.createComponent(instance);
  }

  createComponent(instance = null) {
    const componentFactory: ComponentFactory<AuditoriumFormComponent> =
      this.resolver.resolveComponentFactory(AuditoriumFormComponent);

    this.auditoriumFormRef = this.viewContainerRef.createComponent(componentFactory);
    const componentInstance = this.auditoriumFormRef.instance;

    componentInstance.auditorium = instance?? new Auditorium();
    // componentInstance.buildingModel = this.lecturer;
    componentInstance.option = instance? 'update': 'create';
  }

  deleteAuditorium(id: number) {
    this.auditoriumService.deleteAuditorium(id).subscribe(
      () => location.reload()
    );
  }

  private destroyComponent(componentRef: ComponentRef<any>) {
    componentRef.destroy();
    componentRef = null;
  }

  getAuditoriums(buildingId: number) {
    this.currentBuildingId = buildingId;

    this.auditoriumService.getAuditoriumsByBuilding(buildingId).subscribe(
      data => {
        this.deserializeContent(data);
        this.auditoriums = data;
      }
    );
  }

  listBuildings() {
    this.buildingService.getBuildings().subscribe(
      data => this.buildings = data
    );
  }

  private deserializeContent(data: Auditorium[]) {
    const buildingMap = new Map();

    data.forEach(a => {
      if (typeof a.building === 'object') {
        buildingMap.set(a.building.id, a.building);
      } else if (typeof a.building === 'number') {
        a.building = buildingMap.get(a.building);
      }
    });

  }

}
