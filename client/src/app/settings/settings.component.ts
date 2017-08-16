import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Settings} from '../model/settings';
import {SettingService} from '../services/settings.service';
import {ToastsManager} from 'ng2-toastr';
import {Logger} from 'angular2-logger/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';


@Component({
    selector: 'vr-service',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnChanges {

    @Input()
    settings: Settings;

    arr: number[] = [];

    settingsForm: FormGroup;

    constructor(private settingsService: SettingService, private toastr: ToastsManager, private logger: Logger, private fb: FormBuilder) {
        this.settingsForm = this.fb.group({
            'tickRates': new FormControl('', [Validators.required]),
            'numThreads': new FormControl('', [Validators.required]),
            'numInitPackets': new FormControl('', [Validators.required]),
            'numRamPackets': new FormControl('', [Validators.required]),
            'hostName': new FormControl('', [Validators.required]),
            'startPort': new FormControl('', [Validators.required]),
            'plate-0': new FormControl(''),
            'plate-1': new FormControl(''),
            'plate-2': new FormControl(''),
            'plate-3': new FormControl(''),
            'plate-4': new FormControl('')
        });
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['settings'] && this.settings) {
            this.arr = [];
            for (let i = 0; i < this.settings.overweight.length; i++) {
                this.arr.push(i);
            }
        }
    }

    save() {
        this.settingsService.save(this.settings).subscribe(() => {
            this.toastr.success('Successfully updated settings', 'Success');
        }, (error) => {
            const msg = 'Failed to save settings';
            this.toastr.error(msg, 'Error');
            this.logger.error(msg, error);
        });
    }
}
