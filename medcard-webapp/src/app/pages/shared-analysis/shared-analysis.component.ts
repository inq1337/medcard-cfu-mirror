import {Component, ViewChild} from '@angular/core';
import DataSource from "devextreme/data/data_source";
import {DxDataGridComponent} from "devextreme-angular";
import {environment} from "../../../environments/environment";
import {Analysis, AnalysisParameterStateMapping} from "../../model/analysis";
import {ScreenService} from "../../shared/services";
import CustomStore from "devextreme/data/custom_store";
import {ToolbarPreparingEvent} from "devextreme/ui/data_grid";
import {lastValueFrom} from "rxjs";
import {Page} from "../../model/page";
import notify from "devextreme/ui/notify";
import {ShareService} from "../../services/share.service";
import {onGridOptionsChanged} from "../../shared/services/service-util";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-shared-analysis',
  templateUrl: './shared-analysis.component.html',
  styleUrls: ['./shared-analysis.component.scss'],
  providers: [
    ShareService
  ]
})
export class SharedAnalysisComponent {

  get token(): string {
    return this._token;
  }

  dataSource: DataSource;
  // @ts-ignore
  @ViewChild(DxDataGridComponent, {static: false}) dataGrid: DxDataGridComponent;

  protected readonly onGridOptionsChanged = onGridOptionsChanged;

  downloadUrl: string = environment.api.url;
  mobileMode = false;
  analysisParameterStateMapping = AnalysisParameterStateMapping;
  private readonly _token: string;

  constructor(private shareService: ShareService,
              private activatedRoute: ActivatedRoute,
              private screenService: ScreenService) {
    let t = this.activatedRoute.snapshot.queryParamMap.get('token');
    if (t !== null) {
      this._token = t;
    } else {
      notify('Некорректная ссылка', 'error');
      throw new Error
    }

    this.dataSource = new DataSource(
      new CustomStore({
        load: () => this.load()
      })
    );

    this.mobileMode = this.screenService.sizes['screen-x-small'];
  }

  onToolbarPreparing(e: ToolbarPreparingEvent) {
    if (e.toolbarOptions.items) {
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

  private load(): Promise<any> {
    return lastValueFrom(this.shareService.get(this.token))
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

}
