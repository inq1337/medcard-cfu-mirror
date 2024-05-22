import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {UserProfile} from "../model/user-profile";


@Injectable({
  providedIn: 'root'
})
export class UserService {

  private static SERVICE_API_URL = environment.api.url;

  constructor(private http: HttpClient) {
  }

  getCurrentUser(): Observable<UserProfile> {
    return this.http.get<UserProfile>(UserService.SERVICE_API_URL + '/me');
  }

  updateUserInfo(userAccount: UserProfile): Observable<UserProfile> {
    return this.http.put<UserProfile>(UserService.SERVICE_API_URL + '/me', userAccount);

  }

  updateAvatar(formData: FormData): Observable<string> {
    return this.http.post<any>(UserService.SERVICE_API_URL + `/avatar`, formData);
  }

  moveToPremium(): Observable<string> {
    return this.http.post<string>(UserService.SERVICE_API_URL + '/premium', null);
  }
}
