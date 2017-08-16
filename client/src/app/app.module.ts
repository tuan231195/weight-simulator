import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {SettingsComponent} from './settings/settings.component';
import {HomeComponent} from './home/home.component';
import {Logger} from 'angular2-logger/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {
    MdButtonModule,
    MdCardModule,
    MdIconModule,
    MdInputModule,
    MdMenuModule,
    MdSelectModule,
    MdSnackBarModule,
    MdTabsModule,
    MdToolbarModule,
    MdCheckboxModule, MdSidenavModule, MdListModule
} from '@angular/material';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {ToastModule} from 'ng2-toastr/ng2-toastr';
import {SettingService} from './services/settings.service';
import {WeightService} from './services/weight.service';
import {VehicleComponent} from './vehicle/vehicle.component';
import {VehicleService} from './services/vehicle.service';
import {NumberInputComponent} from './widget/number-input.component';

@NgModule({
    imports: [
        BrowserModule, MdToolbarModule, MdSelectModule,
        ReactiveFormsModule, FormsModule, MdTabsModule, MdCardModule, BrowserAnimationsModule, MdCardModule,
        MdIconModule, MdMenuModule, MdButtonModule, MdInputModule, MdListModule,
        MdSidenavModule, MdSnackBarModule, HttpModule, MdCheckboxModule, ToastModule.forRoot()
    ],
    declarations: [
        AppComponent, SettingsComponent, HomeComponent, VehicleComponent, NumberInputComponent
    ],
    providers: [Logger, SettingService, WeightService, VehicleService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
