import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {routes} from './app.router';
import {AppComponent} from './app.component';
import {ServicesComponent} from './services/services.component';
import {AboutComponent} from './about/about.component';
import {StompService} from './websocket.service';
import {Logger} from 'angular2-logger/core';


@NgModule({
    imports: [
        BrowserModule, routes
    ],
    declarations: [
        AppComponent, AboutComponent, ServicesComponent
    ],
    providers: [StompService, Logger],
    bootstrap: [AppComponent]
})
export class AppModule {
}
