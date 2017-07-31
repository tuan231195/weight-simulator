import {Component, HostListener, OnDestroy, OnInit, ViewContainerRef} from '@angular/core';
import {Subscription} from 'rxjs/Subscription';
import {Logger} from 'angular2-logger/core';
import {Settings} from './model/settings';
import {SettingService} from './services/settings.service';
import {ToastsManager} from 'ng2-toastr';
@Component({
    selector: 'vr-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements  OnInit {

    private settingSubscription: Subscription;
    settings: Settings;

    @HostListener('window:beforeunload')
    ngOnDestroy(): void {
        if (this.settingSubscription) {
            this.settingSubscription.unsubscribe();
        }
    }

    ngOnInit(): void {
        this.settingSubscription = this.settingService.getAll().subscribe((response: Settings) => {
            this.settings = response;
            console.log(this.settings);
        }, (e) => {
            this.logger.error('Failed getting settings', e);
            this.toastr.error('Failed to get application settings', 'Error');
        });
    }

    constructor(private settingService: SettingService, private logger: Logger, private toastr: ToastsManager, private vcf: ViewContainerRef) {
        this.toastr.setRootViewContainerRef(vcf);
    }

}