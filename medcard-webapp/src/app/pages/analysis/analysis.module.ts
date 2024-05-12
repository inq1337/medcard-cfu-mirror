import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
  DxButtonModule,
  DxDataGridModule,
  DxDateBoxModule,
  DxListModule,
  DxPopupModule,
  DxTextBoxModule
} from 'devextreme-angular';
import {RouterModule} from '@angular/router';
import {AnalysisComponent} from "./analysis.component";
import {SecuredPipe} from "../../services/secured.pipe";

@NgModule({
  imports: [
    CommonModule,
    DxDataGridModule,
    RouterModule,
    SecuredPipe,
    DxPopupModule,
    DxButtonModule,
    DxListModule,
    DxDateBoxModule,
    DxTextBoxModule
  ],
  declarations: [
    AnalysisComponent
  ],
  exports: [
    AnalysisComponent
  ]
})
export class AnalysisModule {
}
