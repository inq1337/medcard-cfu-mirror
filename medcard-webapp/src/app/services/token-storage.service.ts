import {Injectable} from '@angular/core';
import {UserProfile} from "../model/user-profile";

const TOKEN_KEY = 'accessToken';
const USER_KEY = 'medcardUser';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  storage: Storage;

  constructor() {
    this.storage = window.localStorage;
  }

  public signOut(): void {
    this.storage.clear();
  }

  public saveToken(token: string): void {
    this.storage.removeItem(TOKEN_KEY);
    this.storage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string | null {
    return this.storage.getItem(TOKEN_KEY);
  }

}
