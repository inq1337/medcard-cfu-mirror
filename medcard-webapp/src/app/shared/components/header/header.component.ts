import {Component, EventEmitter, Input, NgModule, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UserPanelModule} from '../user-panel/user-panel.component';
import {DxButtonModule} from 'devextreme-angular/ui/button';
import {DxToolbarModule} from 'devextreme-angular/ui/toolbar';

import {Router} from '@angular/router';
import {UserProfile} from "../../../model/user-profile";
import {SessionService} from "../../services";

@Component({
  selector: 'app-header',
  templateUrl: 'header.component.html',
  styleUrls: ['./header.component.scss']
})

export class HeaderComponent {
  @Output()
  menuToggle = new EventEmitter<boolean>();

  @Input()
  menuToggleEnabled = false;

  @Input()
  title!: string;

  user: UserProfile | null = null;

  userMenuItems = [{
    text: 'Профиль',
    icon: 'user',
    onClick: () => {
      this.router.navigate(['/profile']);
    }
  },
    {
      text: 'Выйти',
      icon: 'runner',
      onClick: () => {
        this.sessionService.logOut();
      }
    }];

  constructor(private sessionService: SessionService, private router: Router) {
  }

  // ngOnInit() {
  //   // TODO
  //   this.sessionService.getUser().then(value => this.user = value)
  // }

  toggleMenu = () => {
    this.menuToggle.emit();
  }
}

@NgModule({
  imports: [
    CommonModule,
    DxButtonModule,
    UserPanelModule,
    DxToolbarModule
  ],
  declarations: [HeaderComponent],
  exports: [HeaderComponent]
})
export class HeaderModule {
}
