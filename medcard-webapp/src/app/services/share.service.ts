import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "../model/page";
import {Analysis} from "../model/analysis";
import {Injectable} from "@angular/core";
import {ShareRequest} from "../model/share-request";
import {JWTResponse} from "../model/JWT-response";

@Injectable()
export class ShareService {

  private static SERVICE_API_URL = environment.api.url + '/share';

  constructor(private httpClient: HttpClient) {
  }

  get(token: string): Observable<Page<Analysis>> {
    return this.httpClient.get<Page<Analysis>>(`${ShareService.SERVICE_API_URL}/analysis`, {
      headers: new HttpHeaders(this.getHeaders(token))
    });
  }

  createSharedToken(request: ShareRequest): Observable<JWTResponse> {
    return this.httpClient.post<JWTResponse>(`${ShareService.SERVICE_API_URL}/create-link`, request);
  }

  private getHeaders(token: string) {
    return {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Shared-Token': token
    }
  }

}
