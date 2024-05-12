import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Pipe, PipeTransform} from "@angular/core";

@Pipe({
  standalone: true,
  name: 'shared'
})
export class SharedPipe implements PipeTransform {

  constructor(private httpClient: HttpClient) {
  }

  private getHeaders(token: string) {
    return {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Shared-Token': token
    }
  }

  transform(url: string, token: string) {

    return new Observable<string>((observer) => {
      // This is a tiny blank image
      observer.next('data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==');

      // The next and error callbacks from the observer
      const {next, error} = observer;

      this.httpClient.get(url, {
        responseType: 'blob',
        headers: new HttpHeaders(this.getHeaders(token))
      }).subscribe(response => {
        const reader = new FileReader();
        reader.readAsDataURL(response);
        reader.onloadend = () => {
          observer.next(reader.result as string);
        };
      });

      return {
        unsubscribe() {
        }
      };
    });
  }
}
