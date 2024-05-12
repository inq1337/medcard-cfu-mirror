import {Component, ViewChild} from '@angular/core';
import notify from 'devextreme/ui/notify';
import DataSource from 'devextreme/data/data_source';
import CustomStore from 'devextreme/data/custom_store';
import {LoadOptions} from 'devextreme/data';
import {ScreenService} from '../../shared/services';
import {ToolbarPreparingEvent} from "devextreme/ui/data_grid";
import {lastValueFrom} from "rxjs";
import {TemplatesService} from "../../services/templates.service";
import {Page} from "../../model/page";
import {Template} from "../../model/template";
import {onGridOptionsChanged} from "../../shared/services/service-util";
import {DxDataGridComponent} from "devextreme-angular";

@Component({
  selector: 'app-templates',
  templateUrl: './templates.component.html',
  styleUrls: ['./templates.component.scss'],
  providers: [
    TemplatesService
  ]
})
export class TemplatesComponent {

  protected readonly onGridOptionsChanged = onGridOptionsChanged;

  dataSource: DataSource;
  // @ts-ignore
  @ViewChild(DxDataGridComponent, {static: false}) dataGrid: DxDataGridComponent;

  mobileMode = false;

  constructor(private templatesService: TemplatesService,
              private screenService: ScreenService) {
    this.dataSource = new DataSource(
      new CustomStore({
        load: (loadOptions: LoadOptions) => this.load(loadOptions),
        insert: (values) => this.insert(values),
        update: (values, newValues) => this.update(values, newValues),
        remove: (values) => this.remove(values)
      })
    );
    this.mobileMode = this.screenService.sizes['screen-x-small'];
  }

  onToolbarPreparing(e: ToolbarPreparingEvent) {
    if (e.toolbarOptions.items) {
      e.toolbarOptions.items.find(value => value.name === 'searchPanel').location = 'center';
      e.toolbarOptions.items.unshift({
          location: 'before',
          widget: 'dxButton',
          options: {
            icon: 'refresh',
            text: 'Обновить',
            onClick: this.refreshDataGrid.bind(this)
          }
        }
      );
    }
  }

  refreshDataGrid() {
    this.dataGrid.instance.refresh();
  }

  private load(loadOptions: LoadOptions): Promise<any> {
    return lastValueFrom(this.templatesService.getAll(loadOptions))
      .then((response: Page<Template>) => {
        return {
          data: response?.items !== null ? response?.items : [],
          totalCount: response?.totalElements,
          groupCount: response?.totalPages !== 0 ? response?.totalElements / response?.totalPages : 0
        };
      }).catch((e) => {
        console.log(e);
        notify('Ошибка получения шаблонов', 'error');
        throw e && e.error && e.error.description;
      });
  }

  private insert(values: any): Promise<Template> {
    const bank = Object.assign(values) as Template;
    return lastValueFrom(this.templatesService.save(bank))
      .then((created: Template) => {
        notify('Новый шаблон успешно добавлен!', 'success');
        return created;
      }).catch(e => {
        console.log(e);
        notify('Ошибка сохранения шаблона', 'error');
        throw e && e.error && e.error.description;
      });
  }

  private update(values: any, newValues: any): Promise<Template> {
    let assign = Object.assign(values, newValues);
    return this.updateTemplate(assign)
  }

  private remove(event: any) {
    return lastValueFrom(this.templatesService.delete(event.id))
      .then(() => {
        notify('Шаблон успешно удалён!', 'success');
      })
      .catch(e => {
        console.log(e)
        notify('Ошибка удаления шаблона!', 'error');
      })
  }

  public updateTemplate(template: Template): Promise<Template> {
    return lastValueFrom(this.templatesService.update(template))
      .then((updated: Template) => {
        notify('Шаблон успешно обновлен!', 'success');
        return updated;
      }).catch(e => {
        console.log(e);
        notify('Ошибка обновления шаблона', 'error');
        throw e && e.error && e.error.description;
      });
  }

}
