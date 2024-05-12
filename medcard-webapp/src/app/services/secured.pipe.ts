import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Pipe, PipeTransform} from "@angular/core";

@Pipe({
  standalone: true,
  name: 'secured'
})
export class SecuredPipe implements PipeTransform {

  constructor(private httpClient: HttpClient) {
  }

  transform(url: string) {

    return new Observable<string>((observer) => {
      // This is a tiny blank image
      observer.next('data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==');

      // The next and error callbacks from the observer
      const {next, error} = observer;

      this.httpClient.get(url, {responseType: 'blob'}).subscribe(response => {
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
