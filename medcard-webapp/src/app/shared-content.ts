import {CommonModule} from '@angular/common';
import {Component, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

@Component({
  selector: 'app-shared-content',
  template: `
    <router-outlet></router-outlet>
  `,
  styles: [`
    :host {
      width: 100%;
      height: 100%;
    }
  `]
})
export class SharedContentComponent {
}

@NgModule({
  imports: [
    CommonModule,
    RouterModule
  ],
  declarations: [SharedContentComponent],
  exports: [SharedContentComponent]
})
export class SharedContentModule {
}
