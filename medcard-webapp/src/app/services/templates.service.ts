import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {LoadOptions} from "devextreme/data";
import {Observable} from "rxjs";
import {convertToQuery} from "../shared/services/service-util";
import {Template} from "../model/template";
import {Page} from "../model/page";
import {TemplatesSimple} from "../model/templates-simple";

@Injectable()
export class TemplatesService {

  private static SERVICE_API_URL = environment.api.url + '/template';
  private static SERVICE_API_URL_SIMPLE = environment.api.url + '/template-list';

  constructor(private httpClient: HttpClient) {
  }

  getAll(loadOptions: LoadOptions): Observable<Page<Template>> {
    return this.httpClient.get<Page<Template>>(`${TemplatesService.SERVICE_API_URL}${convertToQuery(loadOptions)}`);
  }

  save(template: Template): Observable<Template> {
    return this.httpClient.post<Template>(TemplatesService.SERVICE_API_URL, template);
  }

  update(template: Template): Observable<Template> {
    return this.httpClient.put<Template>(TemplatesService.SERVICE_API_URL + `/${template.id}`, template);
  }

  delete(id: number): Observable<any> {
    return this.httpClient.delete<any>(TemplatesService.SERVICE_API_URL + `/` + id);
  }

  getSimpleList(): Observable<TemplatesSimple> {
    return this.httpClient.get<TemplatesSimple>(`${TemplatesService.SERVICE_API_URL_SIMPLE}`);
  }

}
