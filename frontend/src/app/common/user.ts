export class User {
  id: number;
  firstName: string;
  lastName: string;
  patronymic: string;
  login: string;
  password: string;
  userType: string;
  email: string;

  constructor(id: number = null, firstName: string, lastName: string, patronymic: string,
              login: string, password: string, userType: string, email: string) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.patronymic = patronymic;
    this.login = login;
    this.userType = userType;
    this.password = password;
    this.email = email;
  }
}
