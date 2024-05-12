import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DevExtremeModule, DxDataGridModule} from 'devextreme-angular';
import {RouterModule} from '@angular/router';
import {TemplatesComponent} from './templates.component';

@NgModule({
  imports: [
    CommonModule,
    DxDataGridModule,
    RouterModule,
    DevExtremeModule
  ],
  declarations: [
    TemplatesComponent
  ],
  exports: [
    TemplatesComponent
  ]
})
export class TemplatesModule {
}
