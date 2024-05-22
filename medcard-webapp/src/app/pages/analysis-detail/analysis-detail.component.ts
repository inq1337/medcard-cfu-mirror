import {Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {Analysis, AnalysisParameterStateMapping} from "../../model/analysis";
import {AnalysisService} from "../../services/analysis.service";
import notify from 'devextreme/ui/notify';
import {DxDataGridComponent} from "devextreme-angular";

@Component({
  selector: 'app-analysis-detail',
  templateUrl: './analysis-detail.component.html',
  styleUrls: ['./analysis-detail.component.scss']
})
export class AnalysisDetailComponent {
  // @ts-ignore
  @ViewChild(DxDataGridComponent, {static: false}) dataGrid: DxDataGridComponent;
  // @ts-ignore
  @Input() analysis: Analysis;
  // @ts-ignore
  @Input() downloadUrl: string;
  @Output() analysisUpdated = new EventEmitter<Analysis>();

  analysisParameterStateMapping = AnalysisParameterStateMapping;
  isPopupVisible: boolean = false;
  popupImageName: string = '';

  constructor(private analysisService: AnalysisService) {
  }

  get isCustomTemplate(): boolean {
    return this.analysis?.templateName === 'custom';
  }

  updateAnalysis() {
    this.analysisUpdated.emit(this.analysis);
  }

  updateCommentary() {
    this.updateAnalysis();
  }

  deleteImage(fileName: string) {
    this.analysisService.deleteImage(this.analysis.id, fileName).subscribe({
      next: (response: any) => {
        notify('Изображение удалено', 'success');
        const index = this.analysis.images.indexOf(fileName);
        if (index > -1) {
          this.analysis.images.splice(index, 1);
        }
      },
      error(e) {
        notify('Ошибка при удалении изображения', 'error');
        throw e && e.error && e.error.description;
      }
    });
  }

  openImagePopup(fileName: string) {
    this.popupImageName = fileName;
    this.isPopupVisible = true;
  }

  resetPopupData() {
    this.popupImageName = '';
  }

  fillFromPhotos() {
    this.analysisService.fillFromPhotos(this.analysis.id).subscribe({
      next: (response: Analysis) => {
        notify('Успешно заполнено', 'success');
        this.analysis = response
      },
      error(e) {
        notify('Данный функционал ещё не реализован', 'error');
        throw e && e.error && e.error.description;
      }
    });
  }

  shouldShowStateColumn(): boolean {
    return this.analysis?.parameters.every(param => param.state !== null) || this.analysis?.templateName === 'custom';
  }

  uploadImage() {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = 'image/jpeg';
    input.onchange = (event: any) => {
      const file = event.target.files[0];
      if (file) {
        const formData = new FormData();
        formData.append('image', file, file.name);
        this.analysisService.addImage(this.analysis.id, formData).subscribe({
          next: (response: any) => {
            if (this.analysis.images === null) {
              this.analysis.images = [];
            }
            this.analysis.images.push(response.fileName);
            notify('Изображение добавлено', 'success');
          },
          error(e) {
            notify('Ошибка при добавлении изображения', 'error');
            throw e && e.error && e.error.description;
          }
        });
      }
    };
    input.click();
  }

}
