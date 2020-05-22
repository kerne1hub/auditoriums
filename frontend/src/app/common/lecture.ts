import {Group} from './group';
import {Subject} from './subject';
import {Lecturer} from './lecturer';
import {Auditorium} from './auditorium';

export class Lecture {
  id: number;
  date: Date;
  auditorium: Auditorium | number;
  group: Group | number;
  lecturer: Lecturer | number;
  subject: Subject | number;
}
