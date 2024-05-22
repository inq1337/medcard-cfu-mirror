import {Component, OnInit, ViewChild} from '@angular/core';
import notify from 'devextreme/ui/notify';
import {Page} from '../../model/page';
import DataSource from 'devextreme/data/data_source';
import CustomStore from 'devextreme/data/custom_store';
import {LoadOptions} from 'devextreme/data';
import {DxDataGridComponent} from 'devextreme-angular';
import {ScreenService} from '../../shared/services';
import {lastValueFrom} from "rxjs";
import {AnalysisService} from "../../services/analysis.service";
import {Analysis} from "../../model/analysis";
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
export class AnalysisComponent implements OnInit {

  dataSource: DataSource;
  // @ts-ignore
  @ViewChild(DxDataGridComponent, {static: false}) dataGrid: DxDataGridComponent;

  protected readonly onGridOptionsChanged = onGridOptionsChanged;

  downloadUrl: string = environment.api.url;
  mobileMode = false;

  ngOnInit() {
    this.loadTemplatesSimple();
  }

  isSelected(item: any): boolean {
    return this.selectedItems.indexOf(item) !== -1;
  }

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
    this.mobileMode = this.screenService.sizes['screen-x-small'];
  }

  copyToClipboard(text: string): void {
    navigator.clipboard.writeText(text).then(() => {
      notify('Ссылка скопирована в буфер обмена', 'success');
    }, (err) => {
      notify('Ошибка при копировании ссылки', 'error');
      console.error('Ошибка при копировании в буфер обмена:', err);
    });
  }

  createShareRequest() {
    if (!(this.selectedDate instanceof Date) || isNaN(this.selectedDate.getTime())) {
      notify('Пожалуйста, выберите корректную дату', 'error');
      return;
    }
    let date = this.selectedDate.toISOString();
    let items = this.selectedItems.map(item => item.id);
    this.shareService.createSharedToken(new ShareRequest(items, date)).subscribe({
      next: (response: JWTResponse) => {
        this.sharedUrl = `${window.location.origin}/#/share?token=${response.token}`;
        notify('Ссылка для совместного доступа создана', 'success');
      },
      error: () => {
        notify('Ошибка при создании ссылки для совместного доступа', 'error');
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

  isPopupVisible: boolean = false;
  selectedItems: any[] = [];
  templatesList: TemplatesSimpleItem[] = [];
  // @ts-ignore
  selectedDate: string | number | Date;
  sharedUrl: string | null = null;


  togglePopupVisibility(): void {
    this.isPopupVisible = !this.isPopupVisible;
    if (this.isPopupVisible) {
      this.loadTemplatesSimple()
    }
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
    const selectedTemplate = this.templatesList.find(template => template.name === values.templateName);
    const analysis = {
      ...values,
      templateId: selectedTemplate ? selectedTemplate.id : null
    } as Analysis;

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

}
