import {Injectable} from '@angular/core';
import {Resolve} from '@angular/router';
import {Observable} from 'rxjs';
import {UserProfile} from '../../model/user-profile';
import {UserService} from '../../services/user.service';

@Injectable()
export class ProfileResolver implements Resolve<UserProfile> {
  constructor(private userService: UserService) {
  }

  resolve(): Observable<UserProfile> {
    return this.userService.getCurrentUser();
  }
}
