import { User } from './user';
import {Lecture} from './lecture';

export class Lecturer extends User {
  position: string;
  lectures: Lecture[];

  constructor() {
    super();
  }

  buildLecturer(id: number = null, firstName: string, lastName: string, patronymic: string, login: string,
        password: string, email: string, position: string) {
    this.buildUser(id, firstName, lastName, patronymic, login, password, 'LECTURER', email);
    this.position = position;
    return this;
  }
}
