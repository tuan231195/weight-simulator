import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {SettingsComponent} from './settings/settings.component';
import {HomeComponent} from './home/home.component';
import {StompService} from './websocket.service';
import {Logger} from 'angular2-logger/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {
    MdButtonModule,
    MdCardModule,
    MdIconModule,
    MdMenuModule,
    MdSelectModule,
    MdTabsModule,
    MdToolbarModule
} from '@angular/material';
import {FormsModule} from '@angular/forms';

@NgModule({
    imports: [
        BrowserModule, MdToolbarModule, MdSelectModule,
        FormsModule, MdTabsModule, MdCardModule, BrowserAnimationsModule,
        MdIconModule, MdMenuModule, MdButtonModule
    ],
    declarations: [
        AppComponent, SettingsComponent, HomeComponent
    ],
    providers: [StompService, Logger],
    bootstrap: [AppComponent]
})
export class AppModule {
}
