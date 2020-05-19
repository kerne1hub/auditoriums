import {Group} from './group';
import {Subject} from './subject';
import {Lecturer} from './lecturer';

export class Lecture {
  id: number;
  date: Date;
  group: Group | number;
  subject: Subject | number;
  lecturer: Lecturer | number;
}
