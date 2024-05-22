export class UserProfile {
  email: string;
  firstname: string;
  surname: string;
  patronymic: string;
  avatar: string;
  privilegeLevel: PrivilegeLevel;

  constructor(email: string, firstname: string, surname: string, patronymic: string, avatar: string, privilegeLevel: PrivilegeLevel) {
    this.email = email;
    this.firstname = firstname;
    this.surname = surname;
    this.patronymic = patronymic;
    this.avatar = avatar;
    this.privilegeLevel = privilegeLevel;
  }
}

export enum PrivilegeLevel {
  BASIC = 'basic',
  PREMIUM = 'premium'
}

export const PrivilegeLevelMapping = [
  {id: 'basic', value: 'Базовый'},
  {id: 'premium', value: 'Премиум'}
];

// export enum PrivilegeLevel {
//   BASIC = 'Базовый',
//   PREMIUM = 'premium'
// }
