export class User {
  id: number;
  firstName: string;
  lastName: string;
  patronymic: string;
  login: string;
  password: string;
  email: string;

  constructor(firstName: string, lastName: string, patronymic: string, login: string, password: string, email: string) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.patronymic = patronymic;
    this.login = login;
    this.password = password;
    this.email = email;
  }
}
