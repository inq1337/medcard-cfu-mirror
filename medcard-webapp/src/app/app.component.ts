import {Component, HostBinding} from '@angular/core';
import {AppInfoService, ScreenService, SessionService} from './shared/services';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  @HostBinding('class') get getClass() {
    return Object.keys(this.screen.sizes).filter(cl => this.screen.sizes[cl]).join(' ');
  }

  constructor(private sessionService: SessionService, private screen: ScreenService, public appInfo: AppInfoService) {
  }

  isAuthenticated() {
    return this.sessionService.isLoggedIn;
  }

  isShare(): boolean {
    return this.sessionService.isShare();
  }

}
