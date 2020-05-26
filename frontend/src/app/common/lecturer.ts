import { User } from './user';

export class Lecturer extends User {
  position: string;

  constructor(firstName: string, lastName: string, patronymic: string, login: string, password: string, email: string, position: string) {
    super(firstName, lastName, patronymic, login, password, email);
    this.position = position;
  }
}
