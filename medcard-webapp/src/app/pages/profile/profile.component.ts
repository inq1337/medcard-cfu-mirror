import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {PrivilegeLevel, PrivilegeLevelMapping, UserProfile} from '../../model/user-profile';
import notify from 'devextreme/ui/notify';
import {UserService} from '../../services/user.service';
import {environment} from '../../../environments/environment';
import {ActivatedRoute} from "@angular/router";

@Component({
  templateUrl: 'profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  set userAccount(value: UserProfile) {
    this._userAccount = value;
  }

  get userAccount(): UserProfile {
    return this._userAccount;
  }

  // @ts-ignore
  @ViewChild('fileInput') fileInput: ElementRef;

  // @ts-ignore
  private _userAccount: UserProfile;
  colCountByScreen = {
    xs: 1,
    sm: 2,
    md: 2,
    lg: 3
  };

  avatarUrl = environment.api.url + '/avatar';
  privilegeLevelMapping = PrivilegeLevelMapping

  constructor(private userService: UserService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.userAccount = this.route.snapshot.data["userProfile"];
    // this.userAccount.privilegeLevel = PrivilegeLevel.BASIC;
  }

  update() {
    this.userService.updateUserInfo(this.userAccount).subscribe({
      next: (response: UserProfile) => {
        this.userAccount = response;
        notify('Профиль успешно обновлён', 'success');
      },
      error: (e) => {
        notify('Ошибка обновления профиля', 'error');
        throw e && e.error && e.error.description;
      }
    });
  }

  triggerFileInput() {
    this.fileInput.nativeElement.click();
  }

  onAvatarUpload(event: any) {
    const file = event.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append('image', file, file.name);

      this.userService.updateAvatar(formData).subscribe({
        next: () => {
          notify('Аватар успешно обновлён', 'success');
        },
        error: (e) => {
          notify('Ошибка обновления аватара', 'error');
          throw e && e.error && e.error.description;
        }
      });
    }
  }

  upgradeToPremium() {
    this.userService.moveToPremium().subscribe({
      next: (response: any) => {
        notify('Вы успешно перешли на Премиум', 'success');
        this.userAccount.privilegeLevel = PrivilegeLevel.PREMIUM;
      },
      error: (e) => {
        notify('Данная функция ещё не реализована', 'error');
        throw e && e.error && e.error.description;
      }
    });
  }

  isBasic() {
    return this.userAccount.privilegeLevel === PrivilegeLevel.BASIC;
  }
}
