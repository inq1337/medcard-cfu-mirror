import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
  DxButtonModule,
  DxDataGridModule,
  DxDateBoxModule,
  DxListModule,
  DxPopupModule,
  DxTabPanelModule,
  DxTextAreaModule,
  DxTextBoxModule
} from 'devextreme-angular';
import {RouterModule} from '@angular/router';
import {AnalysisComponent} from "./analysis.component";
import {SecuredPipe} from "../../services/secured.pipe";
import {AnalysisDetailComponent} from "../analysis-detail/analysis-detail.component";

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
    DxTextBoxModule,
    DxTextAreaModule,
    DxTabPanelModule
  ],
  declarations: [
    AnalysisComponent,
    AnalysisDetailComponent
  ],
  exports: [
    AnalysisComponent
  ]
})
export class AnalysisModule {
}
