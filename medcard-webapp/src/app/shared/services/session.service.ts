import {JwtHelperService} from "@auth0/angular-jwt";
import {TokenStorageService} from "../../services/token-storage.service";
import {JWTResponse} from "../../model/JWT-response";
import {Injectable} from "@angular/core";
import {AuthenticationService} from "../../services/authentication.service";
import {AuthRequest} from "../../model/auth-request";
import notify from "devextreme/ui/notify";
import {ActivatedRouteSnapshot, CanActivate, Router} from "@angular/router";
import {SignupRequest} from "../../model/signup.request";

const defaultPath = '/';

@Injectable()
export class SessionService {

  isShare() {
    // console.log(this.router.url.toString())
    return this.router.url.toString().includes('share');
  }

  get lastAuthenticatedPath(): string {
    return this._lastAuthenticatedPath;
  }

  set lastAuthenticatedPath(value: string) {
    this._lastAuthenticatedPath = value;
  }

  loggedIn: boolean = false

  private _lastAuthenticatedPath: string = defaultPath;

  constructor(private tokenStorageService: TokenStorageService,
              private jwtHelper: JwtHelperService,
              private authenticationService: AuthenticationService,
              private router: Router) {
    const token = tokenStorageService.getToken();
    console.log(token);
    this.loggedIn = !!token && !this.jwtHelper.isTokenExpired(token);
    this._lastAuthenticatedPath = defaultPath;
  }

  login(email: string, password: string, rememberMe: boolean) {
    this.authenticationService.auth(new AuthRequest(email, password, rememberMe)).subscribe({
      next: (response: JWTResponse) => {
        this.tokenStorageService.saveToken(response.token)
        this.loggedIn = true
        this.router.navigate([this._lastAuthenticatedPath]);
        // TODO: got permissions from token
        // this.permissionsService.loadPermissions(
        //   getPermissions(JSON.parse(this.jwtHelper.decodeToken(localStorage.getItem('access_token')).permissions))
        // );
      },
      error(e) {
        notify('Ошибка авторизации', 'error');
        throw e && e.error && e.error.description;
      }
    });
  }

  logOut() {
    this.tokenStorageService.signOut();
    this.loggedIn = false;
    this.router.navigate(['/login-form']);
  }

  get isLoggedIn() {
    return this.loggedIn;
  }

  async changePassword(password: any, recoveryCode: string) {

  }

  async createAccount(email: string, password: string, firstname: string, surname: string, patronymic: string) {
    this.authenticationService.signup(new SignupRequest(email, password, firstname, surname, patronymic)).subscribe({
      next: (response: JWTResponse) => {
        this.tokenStorageService.saveToken(response.token)
        this.loggedIn = true
        this.router.navigate([this._lastAuthenticatedPath]);
        // TODO: got permissions from token
        // this.permissionsService.loadPermissions(
        //   getPermissions(JSON.parse(this.jwtHelper.decodeToken(localStorage.getItem('access_token')).permissions))
        // );
      },
      error(e) {
        notify('Ошибка авторизации', 'error');
        throw e && e.error && e.error.description;
      }
    });
  }

  async resetPassword(email: any) {

  }
}

@Injectable()
export class AuthGuardService implements CanActivate {
  constructor(private router: Router,
              private sessionService: SessionService,
              /*private tokenStorageService: TokenStorageService,
              private permissionsService: NgxPermissionsService,
              private jwtHelper: JwtHelperService*/) {
    // if (tokenStorageService.getToken()) {
    //   this.permissionsService.flushPermissions();
    //   const token = this.jwtHelper.decodeToken(localStorage.getItem('access_token'));
    //   if (token && token.permissions) {
    //   this.permissionsService.loadPermissions(
    //     getPermissions(JSON.parse(token.permissions))
    //   );
    //   }
    // }
  }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const isLoggedIn = this.sessionService.loggedIn;
    const isAuthForm = [
      'login-form',
      'reset-password',
      'create-account',
      'change-password/:recoveryCode'
    ].includes(route.routeConfig?.path || defaultPath);
    let share = route.routeConfig?.path?.includes('share');
    if (isLoggedIn && isAuthForm) {
      this.sessionService.lastAuthenticatedPath = defaultPath;
      this.router.navigate([defaultPath]);
      return false;
    }

    if (!isLoggedIn && !isAuthForm && !share) {
      console.log('redirect')
      this.router.navigate(['/login-form']);
    }

    if (isLoggedIn) {
      this.sessionService.lastAuthenticatedPath = route.routeConfig?.path || defaultPath;
    }

    if (share) {
      return true;
    }
    return isLoggedIn || isAuthForm;
  }
}
