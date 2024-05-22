import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {TemplatesComponent} from './templates.component';
import {TemplatesDetailComponent} from "../templates-detail/templates-detail.component";
import {DevExtremeModule, DxDataGridModule} from "devextreme-angular";

@NgModule({
  imports: [
    CommonModule,
    DxDataGridModule,
    RouterModule,
    DxDataGridModule,
    DevExtremeModule
  ],
  declarations: [
    TemplatesComponent,
    TemplatesDetailComponent
  ],
  exports: [
    TemplatesComponent
  ]
})
export class TemplatesModule {
}
