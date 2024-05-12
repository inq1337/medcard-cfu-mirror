import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthRequest} from "../model/auth-request";
import {JWTResponse} from "../model/JWT-response";
import {SignupRequest} from "../model/signup.request";

@Injectable()
export class AuthenticationService {

  private static AUTH_API_URL = environment.api.url + '/auth';
  private static SIGNUP_API_URL = environment.api.url + '/signup';

  constructor(private http: HttpClient) {
  }

  auth(authRequest: AuthRequest): Observable<JWTResponse> {
    return this.http.post<JWTResponse>(AuthenticationService.AUTH_API_URL, authRequest);
  }

  signup(signupRequest: SignupRequest): Observable<JWTResponse> {
    return this.http.post<JWTResponse>(AuthenticationService.SIGNUP_API_URL, signupRequest);
  }

}
