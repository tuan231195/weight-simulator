import {Component, Input} from '@angular/core';
import {Settings} from '../model/settings';


@Component({
    selector: 'vr-service',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.css']
})
export class SettingsComponent {
    @Input()
    settings: Settings;
}
