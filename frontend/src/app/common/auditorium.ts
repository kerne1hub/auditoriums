import {Building} from './building';
import {Lecture} from './lecture';

export class Auditorium {
  id: number;
  name: string;
  capacity: number;
  active: boolean;
  building: Building;
  lectures: Lecture[];


  constructor() {
  }
}
