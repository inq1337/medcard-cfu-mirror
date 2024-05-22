import {Component, Input, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {DxListModule} from 'devextreme-angular/ui/list';
import {DxContextMenuModule} from 'devextreme-angular/ui/context-menu';
import {UserProfile} from "../../../model/user-profile";
import notify from "devextreme/ui/notify";
import {UserService} from "../../../services/user.service";
import {SecuredPipe} from "../../../services/secured.pipe";
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-user-panel',
  templateUrl: 'user-panel.component.html',
  styleUrls: ['./user-panel.component.scss']
})

export class UserPanelComponent {
  @Input()
  menuItems: any;

  @Input()
  menuMode!: string;

  user!: UserProfile | null;
  downloadUrl: string = environment.api.url;


  constructor(private userService: UserService) {
    this.userService.getCurrentUser().subscribe(response => {
      this.user = response;
      console.log(response);
    }, e => {
      console.log(e);
      notify('Ошибка загрузки профиля', 'error');
      throw e && e.error && e.error.description;
    });
  }
}

@NgModule({
  imports: [
    DxListModule,
    DxContextMenuModule,
    CommonModule,
    SecuredPipe
  ],
  declarations: [UserPanelComponent],
  exports: [UserPanelComponent]
})
export class UserPanelModule {
}
