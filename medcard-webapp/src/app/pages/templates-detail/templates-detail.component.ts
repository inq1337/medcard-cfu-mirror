import {Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {Template} from "../../model/template";
import {DxDataGridComponent} from "devextreme-angular";

@Component({
  selector: 'app-templates-detail',
  templateUrl: './templates-detail.component.html',
  styleUrls: ['./templates-detail.component.scss']
})
export class TemplatesDetailComponent {
  // @ts-ignore
  @ViewChild(DxDataGridComponent, {static: false}) dataGrid: DxDataGridComponent;
  // @ts-ignore
  @Input() template: Template;
  @Output() templateUpdated = new EventEmitter<Template>();

  updateTemplate() {
    this.templateUpdated.emit(this.template);
  }

}
