import {Component} from '@angular/core';
import {UserProfile} from "../../model/user-profile";
import notify from "devextreme/ui/notify";
import {UserService} from "../../services/user.service";
import {environment} from "../../../environments/environment";


@Component({
  templateUrl: 'profile.component.html',
  styleUrls: ['./profile.component.scss']
})

export class ProfileComponent {

  // @ts-ignore
  userAccount: UserProfile;
  colCountByScreen = {
    xs: 1,
    sm: 2,
    md: 3,
    lg: 4
  };
  downloadUrl: string = environment.api.url;


  constructor(private userService: UserService) {
    this.userService.getCurrentUser().subscribe({
      next: (response: UserProfile) => {
        this.userAccount = response
      },
      error(e) {
        notify('Ошибка авторизации', 'error');
        throw e && e.error && e.error.description;
      }
    });
  }

  update() {

  }

  onAvatarUploaded($event: any) {

  }
}
