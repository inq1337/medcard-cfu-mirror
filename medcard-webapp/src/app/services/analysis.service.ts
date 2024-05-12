import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {LoadOptions} from "devextreme/data";
import {Observable} from "rxjs";
import {convertToQuery} from "../shared/services/service-util";
import {Analysis} from "../model/analysis";
import {Page} from "../model/page";

@Injectable()
export class AnalysisService {

  private static SERVICE_API_URL = environment.api.url + '/analysis';

  constructor(private httpClient: HttpClient) {
  }

  getAll(loadOptions: LoadOptions): Observable<Page<Analysis>> {
    return this.httpClient.get<Page<Analysis>>(`${AnalysisService.SERVICE_API_URL}${convertToQuery(loadOptions)}`);
  }

  save(analysis: Analysis): Observable<Analysis> {
    return this.httpClient.post<Analysis>(AnalysisService.SERVICE_API_URL, analysis);
  }

  update(analysis: Analysis): Observable<Analysis> {
    return this.httpClient.put<Analysis>(AnalysisService.SERVICE_API_URL + `/${analysis.id}`, analysis);
  }

  delete(id: number): Observable<any> {
    return this.httpClient.delete<any>(AnalysisService.SERVICE_API_URL + `/` + id);
  }

  deleteImage(analysisId: number, fileName: string): Observable<any> {
    return this.httpClient.delete<any>(AnalysisService.SERVICE_API_URL + `/` + analysisId + `/` + `image/` + fileName);
  }

  addImage(analysisId: number, formData: FormData): Observable<string> {
    return this.httpClient.post<any>(AnalysisService.SERVICE_API_URL + `/` + analysisId + `/` + `image`, formData);
  }

}
