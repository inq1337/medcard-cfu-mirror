import {CommonModule} from '@angular/common';
import {Component, NgModule} from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {ValidationCallbackData} from 'devextreme-angular/common';
import {DxFormModule} from 'devextreme-angular/ui/form';
import {DxLoadIndicatorModule} from 'devextreme-angular/ui/load-indicator';
import {SessionService} from "../../services";


@Component({
  selector: 'app-create-account-form',
  templateUrl: './create-account-form.component.html',
  styleUrls: ['./create-account-form.component.scss']
})
export class CreateAccountFormComponent {
  loading = false;
  formData: any = {};

  constructor(private sessionService: SessionService, private router: Router) {
  }

  async onSubmit(e: Event) {
    e.preventDefault();
    const {firstname, surname, patronymic, email, password} = this.formData;
    this.loading = true;

    console.log('firstname ', firstname);
    console.log('surname ', surname);
    console.log('patronymic ', patronymic);
    console.log('email ', email);
    console.log('password ', password);
    const result = await this.sessionService.createAccount(email, password, firstname, surname, patronymic);
    this.loading = false;

    // if (result.isOk) {
    //   this.router.navigate(['/login-form']);
    // } else {
    //   notify(result.message, 'error', 2000);
    // }
  }

  confirmPassword = (e: ValidationCallbackData) => {
    return e.value === this.formData.password;
  }
}

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    DxFormModule,
    DxLoadIndicatorModule
  ],
  declarations: [CreateAccountFormComponent],
  exports: [CreateAccountFormComponent]
})
export class CreateAccountFormModule {
}
