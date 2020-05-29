import {Group} from './group';
import {Subject} from './subject';
import {Lecturer} from './lecturer';
import {Auditorium} from './auditorium';

export class Lecture {
  id: number;
  date: Date;
  auditorium: Auditorium;
  group: Group;
  lecturer: Lecturer;
  subject: Subject;

  constructor() {
  }
}
