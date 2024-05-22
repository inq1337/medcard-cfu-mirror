import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {SideNavInnerToolbarModule, SideNavOuterToolbarModule, SingleCardModule} from './layouts';
import {
  ChangePasswordFormModule,
  CreateAccountFormModule,
  FooterModule,
  LoginFormModule,
  ResetPasswordFormModule
} from './shared/components';
import {AppInfoService, ScreenService, SessionService} from './shared/services';
import {UnauthenticatedContentModule} from './unauthenticated-content';
import {AppRoutingModule} from './app-routing.module';
import {environment} from "../environments/environment";
import {TokenStorageService} from "./services/token-storage.service";
import {HttpClientModule} from "@angular/common/http";
import {JWT_OPTIONS, JwtModule} from "@auth0/angular-jwt";
import {AuthenticationService} from "./services/authentication.service";
import {TemplatesModule} from "./pages/templates/templates.module";
import {AnalysisModule} from "./pages/analysis/analysis.module";
import {SharedAnalysisComponent} from './pages/shared-analysis/shared-analysis.component';
import {
  DxButtonModule,
  DxDataGridModule,
  DxDateBoxModule,
  DxListModule,
  DxPopupModule,
  DxTabPanelModule,
  DxTemplateModule,
  DxTextAreaModule,
  DxTextBoxModule
} from "devextreme-angular";
import {
  DxiColumnModule,
  DxiValidationRuleModule,
  DxoEditingModule,
  DxoFilterRowModule,
  DxoLookupModule,
  DxoMasterDetailModule,
  DxoPagerModule,
  DxoPagingModule,
  DxoRemoteOperationsModule,
  DxoSearchPanelModule
} from "devextreme-angular/ui/nested";
import {SecuredPipe} from "./services/secured.pipe";
import {SharedContentModule} from "./shared-content";
import {NgOptimizedImage} from "@angular/common";
import {SharedPipe} from "./services/shared.pipe";
import {ProfileResolver} from "./pages/profile/profile-resolver";

export function jwtOptionsFactory(tokenService: TokenStorageService) {
  return {
    tokenGetter: () => {
      return tokenService.getToken();
    },
    headerName: 'Authorization',
    authScheme: 'Bearer ',
    allowedDomains: environment.httpClient.whitelistedDomains,
    disallowedRoutes: environment.httpClient.blacklistedRoutes
  }
}

@NgModule({
  declarations: [
    AppComponent,
    SharedAnalysisComponent
  ],
  imports: [
    JwtModule.forRoot({
      jwtOptionsProvider: {
        provide: JWT_OPTIONS,
        useFactory: jwtOptionsFactory,
        deps: [TokenStorageService]
      }
    }),
    // NgxPermissionsModule.forRoot(),
    HttpClientModule,
    BrowserModule,
    SideNavOuterToolbarModule,
    SideNavInnerToolbarModule,
    SingleCardModule,
    FooterModule,
    ResetPasswordFormModule,
    CreateAccountFormModule,
    ChangePasswordFormModule,
    LoginFormModule,
    UnauthenticatedContentModule,
    AppRoutingModule,
    TemplatesModule,
    AnalysisModule,
    DxDataGridModule,
    DxTemplateModule,
    DxiColumnModule,
    DxiValidationRuleModule,
    DxoEditingModule,
    DxoFilterRowModule,
    DxoLookupModule,
    DxoMasterDetailModule,
    DxoPagerModule,
    DxoPagingModule,
    DxoRemoteOperationsModule,
    DxoSearchPanelModule,
    SecuredPipe,
    UnauthenticatedContentModule,
    SharedContentModule,
    NgOptimizedImage,
    SecuredPipe,
    SharedPipe,
    DxButtonModule,
    DxTextAreaModule,
    DxTabPanelModule,
    DxDateBoxModule,
    DxListModule,
    DxPopupModule,
    DxTextBoxModule
  ],
  providers: [
    SessionService,
    ScreenService,
    AppInfoService,
    AuthenticationService,
    ProfileResolver
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
