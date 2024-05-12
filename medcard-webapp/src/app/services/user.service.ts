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

}
