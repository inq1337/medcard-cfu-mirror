import {CommonModule} from '@angular/common';
import {Component, NgModule} from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {DxFormModule} from 'devextreme-angular/ui/form';
import {DxLoadIndicatorModule} from 'devextreme-angular/ui/load-indicator';
import {SessionService} from "../../services";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent {
  loading: boolean = false;
  formData: any = {};

  constructor(private sessionService: SessionService,
              private router: Router) {
  }

  async onSubmit(e: Event) {
    e.preventDefault();
    const {email, password, rememberMe} = this.formData;
    // this.loading = true;
    this.sessionService.login(email, password, rememberMe);
  }

  onCreateAccountClick = () => {
    this.router.navigate(['/create-account']);
  }
}

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    DxFormModule,
    DxLoadIndicatorModule,
  ],
  declarations: [LoginFormComponent],
  exports: [LoginFormComponent]
})
export class LoginFormModule {
}
