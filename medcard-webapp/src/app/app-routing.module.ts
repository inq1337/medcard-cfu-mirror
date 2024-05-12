import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {
  ChangePasswordFormComponent,
  CreateAccountFormComponent,
  LoginFormComponent,
  ResetPasswordFormComponent
} from './shared/components';
import {AuthGuardService} from './shared/services';
import {HomeComponent} from './pages/home/home.component';
import {ProfileComponent} from './pages/profile/profile.component';
import {DxButtonModule, DxDataGridModule, DxFormModule} from 'devextreme-angular';
import {TemplatesComponent} from "./pages/templates/templates.component";
import {AnalysisComponent} from "./pages/analysis/analysis.component";
import {SecuredPipe} from "./services/secured.pipe";
import {AsyncPipe} from "@angular/common";
import {SharedAnalysisComponent} from "./pages/shared-analysis/shared-analysis.component";

const routes: Routes = [
  {
    path: 'share',
    component: SharedAnalysisComponent,
    canActivate: [AuthGuardService]

  },
  {
    path: 'analysis',
    component: AnalysisComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'templates',
    component: TemplatesComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'login-form',
    component: LoginFormComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'reset-password',
    component: ResetPasswordFormComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'create-account',
    component: CreateAccountFormComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: 'change-password/:recoveryCode',
    component: ChangePasswordFormComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: '**',
    redirectTo: 'home'
  }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, {useHash: true}), DxDataGridModule, DxFormModule, SecuredPipe, AsyncPipe, DxButtonModule],
  providers: [AuthGuardService],
  exports: [RouterModule],
  declarations: [
    HomeComponent,
    ProfileComponent,
  ]
})
export class AppRoutingModule {
}
