import {Component, OnDestroy, OnInit} from '@angular/core';
import {Plate} from '../model/plate';
import {Http} from '@angular/http';
import {Subscription} from 'rxjs/Subscription';
import {PlateConfig} from '../model/plate-config';
import {Logger} from 'angular2-logger/core';
import {MdSnackBar} from '@angular/material';
import 'rxjs/add/operator/map';
import {VehicleType} from '../model/vehicle-type';


@Component({
    selector: 'vr-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {

    selectedPlate: number;
    selected: boolean = false;
    selectedVehicle: number;
    isRunning: boolean = false;
    plates: Plate[];
    plateConfigs: PlateConfig[];
    vehicleTypes: VehicleType[];
    plateSubscription: Subscription;
    vehicleSubscription: Subscription;

    constructor(private http: Http, private logger: Logger, private snackbar: MdSnackBar) {
        this.plates = [
            {plateNum: 1, scaleActive: true, scaleJoin: false, weight: 4.5, port: 2002},
            {plateNum: 2, scaleActive: false, scaleJoin: true, weight: 5, port: 2003},
            {plateNum: 3, scaleActive: true, scaleJoin: false, weight: 6, port: 2004},
            {plateNum: 4, scaleActive: true, scaleJoin: true, weight: 2.5, port: 2005}
        ];
    }

    ngOnInit(): void {
        this.plateSubscription = this.http.get('/api/plates').map(response => response.json()).subscribe((data) => {
            this.plateConfigs = data;
        }, (e) => {
            const msg = 'Failed to get plate configurations';
            this.snackbar.open(msg, null, {
                duration: 2000
            });
            this.logger.error(msg, e);
        });
    }

    ngOnDestroy(): void {
        this.plateSubscription.unsubscribe();
        if (this.vehicleSubscription)
            this.vehicleSubscription.unsubscribe();
    }


    select() {
        setTimeout(() => {
            this.selected = true;
        }, 200);
        this.http.get(`/api/plates/${this.selectedPlate}/vtypes`)
            .map(response => response.json())
            .subscribe((data) => {
                this.vehicleTypes = data;
            }, (e) => {
                const msg = 'Failed to get vehicle types for selected plate';
                this.snackbar.open(msg, null, {
                    duration: 2000
                });
                this.logger.error(msg, e);
            });

    }

    unselect() {
        setTimeout(() => {
            this.selected = false;
        }, 200);
    }

    run() {
        this.isRunning = true;
    }

    next() {
    }

    stop() {
        this.isRunning = false;
    }


    getIcon(state: boolean) {
        return state ? 'done' : 'close';
    }

    getClass(state: boolean) {
        return state ? 'primary' : 'warn';
    }
}
