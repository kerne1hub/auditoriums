import { Component, OnInit } from '@angular/core';
import {Auditorium} from '../../common/auditorium';
import {AuditoriumService} from '../../services/auditorium.service';

@Component({
  selector: 'app-auditorium-list',
  templateUrl: './auditorium-list.component.html',
  styleUrls: ['./auditorium-list.component.css']
})
export class AuditoriumListComponent implements OnInit {

  auditoriums: Auditorium[];
  constructor(private auditoriumService: AuditoriumService) { }

  ngOnInit(): void {
    this.listAuditoriums();
  }

  listAuditoriums() {
    this.auditoriumService.getAuditoriums().subscribe(
      data => this.auditoriums = data
    );
  }

}
