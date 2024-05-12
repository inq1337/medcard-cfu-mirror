export class SignupRequest {

  email: String
  password: String
  firstname: String
  surname: String
  patronymic: String

  constructor(email: String, password: String, firstname: String, surname: String, patronymic: String) {
    this.email = email;
    this.password = password;
    this.firstname = firstname;
    this.surname = surname;
    this.patronymic = patronymic;
  }

}
