export class UserProfile {
  email: string
  firstname: string
  surname: string
  patronymic: string
  avatar: string

  constructor(email: string, firstname: string, surname: string, patronymic: string, avatar: string) {
    this.email = email;
    this.firstname = firstname;
    this.surname = surname;
    this.patronymic = patronymic;
    this.avatar = avatar;
  }

}
