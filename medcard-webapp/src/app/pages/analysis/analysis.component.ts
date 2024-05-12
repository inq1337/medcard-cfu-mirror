import {Component, ViewChild} from '@angular/core';
import notify from 'devextreme/ui/notify';
import {Page} from '../../model/page';
import DataSource from 'devextreme/data/data_source';
import CustomStore from 'devextreme/data/custom_store';
import {LoadOptions} from 'devextreme/data';
import {DxDataGridComponent} from 'devextreme-angular';
import {ScreenService} from '../../shared/services';
import {ToolbarPreparingEvent} from "devextreme/ui/data_grid";
import {lastValueFrom} from "rxjs";
import {AnalysisService} from "../../services/analysis.service";
import {Analysis, AnalysisParameterStateMapping} from "../../model/analysis";
import {onGridOptionsChanged} from "../../shared/services/service-util";
import {TemplatesService} from "../../services/templates.service";
import {environment} from "../../../environments/environment";
import {ShareService} from "../../services/share.service";
import {ShareRequest} from "../../model/share-request";
import {TemplatesSimple, TemplatesSimpleItem} from "../../model/templates-simple";
import {JWTResponse} from "../../model/JWT-response";

@Component({
  selector: 'app-analysis',
  templateUrl: './analysis.component.html',
  styleUrls: ['./analysis.component.scss'],
  providers: [
    AnalysisService,
    TemplatesService,
    ShareService
  ]
})
export class AnalysisComponent {

  dataSource: DataSource;
  // @ts-ignore
  @ViewChild(DxDataGridComponent, {static: false}) dataGrid: DxDataGridComponent;

  protected readonly onGridOptionsChanged = onGridOptionsChanged;

  downloadUrl: string = environment.api.url;
  mobileMode = false;
  analysisParameterStateMapping = AnalysisParameterStateMapping;

  constructor(private analysisService: AnalysisService,
              private templatesService: TemplatesService,
              private shareService: ShareService,
              private screenService: ScreenService) {
    this.dataSource = new DataSource(
      new CustomStore({
        load: (loadOptions: LoadOptions) => this.load(loadOptions),
        insert: (values) => this.insert(values),
        update: (values, newValues) => this.update(values, newValues),
        remove: (values) => this.remove(values)
      })
    );
    console.log(this.dataSource)
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
        },
        {
          location: 'after',
          widget: 'dxButton',
          options: {
            icon: 'share',
            text: 'Поделиться анализами',
            onClick: () => this.togglePopupVisibility()
          }
        }
      );
    }
  }

  isPopupVisible: boolean = false;
  selectedItems: any[] = [];
  templatesList: TemplatesSimpleItem[] = [];
  selectedDate: string | number | Date = "";
  sharedUrl: string | null = null;

  copyToClipboard(text: string): void {
    navigator.clipboard.writeText(text).then(() => {
      notify('Ссылка скопирована в буфер обмена', 'success');
    }, (err) => {
      notify('Ошибка при копировании ссылки', 'error');
      console.error('Ошибка при копировании в буфер обмена:', err);
    });
  }

  createShareRequest() {
    let date = this.selectedDate instanceof Date ? this.selectedDate.toISOString() : null;
    let items = this.selectedItems.map(item => item.id);
    this.shareService.createSharedToken(new ShareRequest(items, date)).subscribe({
      next: (response: JWTResponse) => {
        this.sharedUrl = `${window.location.origin}/#/share?token=${response.accessToken}`;
        notify('Ссылка успешно создана', 'success');
      },
      error(e) {
        notify('Ошибка при создании ссылки', 'error');
        throw e && e.error && e.error.description;
      }
    });
  }

  loadTemplatesSimple(): void {
    this.templatesService.getSimpleList().subscribe({
      next: (response: TemplatesSimple) => {
        this.templatesList = response.templates;
      },
      error(e) {
        notify('Ошибка при получении шаблонов', 'error');
        throw e && e.error && e.error.description;
      }
    });
  }

  togglePopupVisibility(): void {
    this.isPopupVisible = !this.isPopupVisible;
    if (this.isPopupVisible) {
      this.loadTemplatesSimple();
    }
  }

  refreshDataGrid() {
    this.dataGrid.instance.refresh();
  }

  private load(loadOptions: LoadOptions): Promise<any> {
    return lastValueFrom(this.analysisService.getAll(loadOptions))
      .then((response: Page<Analysis>) => {
        return {
          data: response?.items !== null ? response?.items : [],
          totalCount: response?.totalElements,
          groupCount: response?.totalPages !== 0 ? response?.totalElements / response?.totalPages : 0
        };
      }).catch((e) => {
        console.log(e);
        notify('Ошибка получения списка анализов', 'error');
        throw e && e.error && e.error.description;
      });
  }

  private insert(values: any): Promise<Analysis> {
    const analysis = Object.assign(values) as Analysis;

    return lastValueFrom(this.analysisService.save(analysis))
      .then((created: Analysis) => {
        notify('Новый анализ успешно добавлен!', 'success');
        return created;
      }).catch(e => {
        console.log(e);
        notify('Ошибка сохранения анализа', 'error');
        throw e && e.error && e.error.description;
      });
  }

  private update(values: any, newValues: any): Promise<Analysis> {
    let analysis = Object.assign(values, newValues);
    return this.updateAnalysis(analysis)
  }

  private remove(event: any) {
    return lastValueFrom(this.analysisService.delete(event.id))
      .then(() => {
        notify('Анализ успешно удалён!', 'success');
      })
      .catch(e => {
        console.log(e)
        notify('Ошибка удаления анализа!', 'error');
      })
  }

  updateAnalysis(analysis: Analysis): Promise<Analysis> {
    return lastValueFrom(this.analysisService.update(analysis))
      .then((updated: Analysis) => {
        notify('Анализ успешно обновлен!', 'success');
        return updated;
      }).catch(e => {
        console.log(e);
        notify('Ошибка обновления анализа', 'error');
        throw e && e.error && e.error.description;
      });
  }

  deleteImage(analysis: Analysis, fileName: string) {
    this.analysisService.deleteImage(analysis.id, fileName).subscribe({
      next: (response: any) => {
        notify('Изображение удалено', 'success');
        const index = analysis.images.indexOf(fileName);
        if (index > -1) {
          analysis.images.splice(index, 1);
        }
      },
      error(e) {
        notify('Ошибка при удалении изображения', 'error');
        throw e && e.error && e.error.description;
      }
    });
  }

  onFileSelected(event: any, analysis: Analysis) {
    const file = event.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append('image', file, file.name);
      this.analysisService.addImage(analysis.id, formData).subscribe({
        next: (response: any) => {
          if (analysis.images === null) {
            analysis.images = [];
          }
          analysis.images.push(response.fileName)
          notify('Изображение добавлено', 'success');
        },
        error(e) {
          notify('Ошибка при добавлении изображения', 'error');
          throw e && e.error && e.error.description;
        }
      });
    }
  }

}
