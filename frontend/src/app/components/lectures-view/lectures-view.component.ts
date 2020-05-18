import {Component, Input, OnInit} from '@angular/core';
import {Lecture} from '../../common/lecture';

@Component({
  selector: 'app-lectures-view',
  templateUrl: './lectures-view.component.html',
  styleUrls: ['./lectures-view.component.css']
})
export class LecturesViewComponent implements OnInit {

  @Input() lectures: Lecture[];

  constructor() { }

  ngOnInit(): void {
    console.log(this.lectures);
  }

}
