import {Component, ElementRef, HostListener, Input, OnInit, ViewContainerRef} from '@angular/core';
import {Subscription} from 'rxjs/Subscription';
import {PlateConfig} from '../model/plate-config';
import {Logger} from 'angular2-logger/core';
import {MdSnackBar} from '@angular/material';
import 'rxjs/add/operator/map';
import {VehicleType} from '../model/vehicle-type';
import {ToastsManager} from 'ng2-toastr';
import {Settings} from '../model/settings';
import {WeightService} from '../services/weight.service';
import {VehicleState} from '../model/vehicle-state';
import {SimState} from '../model/sim-state';
let $ = require('jquery');

@Component({
    selector: 'vr-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

    selectedPlateConfig: PlateConfig;
    selected: boolean = false;
    selectedVehicle: string;
    isRunning: boolean = false;
    vehicleStates: VehicleState[];
    interval;
    plateConfigs: PlateConfig[];
    vehicleTypes: VehicleType[];
    plateSubscription: Subscription;
    vehicleSubscription: Subscription;
    simulateSubscription: Subscription;
    stopSubscription: Subscription;
    nextSubscription: Subscription;
    simState: SimState;
    @Input()
    settings: Settings;
    vehicleImage: any;
    imageContainer: any;


    constructor(private weighService: WeightService, private el: ElementRef, private logger: Logger, private snackbar: MdSnackBar, private toastr: ToastsManager, private vcr: ViewContainerRef) {

    }

    ngOnInit(): void {
        this.plateSubscription = this.weighService.getAllPlateConfigs().subscribe((data: PlateConfig[]) => {
            this.plateConfigs = data;
        }, (e) => {
            const msg = 'Failed to get plate configurations';
            this.toastr.error(msg, 'Error', {toastLife: 4000});
            this.logger.error(msg, e);
        });
    }


    @HostListener('window:beforeunload')
    onDestroy(): void {
        if (this.isRunning)
            this.weighService.stop().subscribe();
        this.plateSubscription.unsubscribe();
        if (this.vehicleSubscription)
            this.vehicleSubscription.unsubscribe();
        if (this.simulateSubscription)
            this.simulateSubscription.unsubscribe();
        if (this.stopSubscription)
            this.stopSubscription.unsubscribe();
        if (this.nextSubscription)
            this.nextSubscription.unsubscribe();

    }


    select() {
        this.isRunning = false;
        this.selectedVehicle = null;
        this.reset();
        setTimeout(() => {
            this.selected = true;
        }, 200);
        if (this.vehicleSubscription != null)
            this.vehicleSubscription.unsubscribe();
        this.vehicleSubscription = this.weighService.getAllVtypes(this.selectedPlateConfig.num)
            .subscribe((data: VehicleType[]) => {
                this.vehicleTypes = data;
                this.reset();
            }, (e) => {
                const msg = 'Failed to get vehicle types for selected plate';
                this.toastr.error(msg, 'Error', {toastLife: 4000});
                this.logger.error(msg, e);
            });

    }

    unselect() {
        setTimeout(() => {
            this.selected = false;
        }, 200);
    }

    reset() {
        this.vehicleStates = [];
        for (let i = 0; i < this.selectedPlateConfig.num; i++) {
            let vehicleState = new VehicleState();
            vehicleState.plateNum = i + 1;
            vehicleState.port = this.selectedPlateConfig.port + i;
            vehicleState.weight = 0;
            vehicleState.scaleJoin = false;
            vehicleState.scaleActive = false;
            this.vehicleStates.push(vehicleState);
        }
        this.simState = null;
    }

    run() {
        this.vehicleImage = $('.vehicle-image');
        this.imageContainer = $('.image-container');
        this.vehicleImage.show();
        setTimeout(() => {
            this.vehicleImage.css({left: -this.vehicleImage.width() + 'px'});
        }, 50);
        if (this.simulateSubscription) {
            this.simulateSubscription.unsubscribe();
        }
        this.simulateSubscription = this.weighService.simulate(this.selectedPlateConfig, this.selectedVehicle).subscribe((response) => {
            this.isRunning = true;
            let tickRates = 500;
            let lastAnimateTime = Date.now();
            if (this.settings && this.settings.tickRates) {
                tickRates = this.settings.tickRates * 1000;
            }
            let animateRate = 2 * tickRates;
            this.interval = setInterval(() => {
                this.weighService.getState().subscribe((simState: SimState) => {
                    this.vehicleStates = simState.vehicleStates;
                    for (let i = 0; i < this.vehicleStates.length; i++) {
                        this.vehicleStates[i].plateNum = i + 1;
                    }
                    let left = simState.currentVehiclePosition - this.vehicleImage.width();
                    
                    let now = Date.now();
                    if (now - lastAnimateTime > tickRates) {
                        lastAnimateTime = now;
                        this.vehicleImage.animate({left: left + 'px'}, tickRates, () => {
                            this.vehicleImage.css({left: left + 'px'});
                            if (this.simState.step === this.simState.numSteps) {
                                this.isRunning = false;
                            }
                        });
                    }
                    this.simState = simState;
                    if (this.simState.step === this.simState.numSteps) {
                        clearInterval(this.interval);
                    }
                });
            }, tickRates);
        }, (error) => {
            const msg = 'Failed to start the simulation';
            this.logger.error(msg, error);
            if (typeof error === 'string') {
                this.toastr.error(msg + ': ' + error, 'Error');
            }
        });
    }

    next() {
        if (this.nextSubscription)
            this.nextSubscription.unsubscribe();
        this.nextSubscription = this.weighService.nextStep().subscribe((response) => {
        }, (error) => {
            const msg = 'Failed to move to next step';
            this.logger.error(msg, error);
            if (typeof error === 'string') {
                this.toastr.error(msg + ': ' + error, 'Error');
            }
        });
    }

    stop() {
        if (this.stopSubscription)
            this.stopSubscription.unsubscribe();

        if (this.nextSubscription)
            this.nextSubscription.unsubscribe();

        if (this.simState.step + 1 == this.simState.numSteps) {
            this.weighService.nextStep().subscribe();
        }
        else {
            this.stopSubscription = this.weighService.stop().subscribe((response) => {
                clearInterval(this.interval);
                //end the simulation
                this.vehicleImage.fadeOut(800, () => {
                    this.isRunning = false;
                    this.reset();
                });
            }, (error) => {
                const msg = 'Failed to stop the simulation';
                this.logger.error(msg, error);
                if (typeof error === 'string') {
                    this.toastr.error(msg + ': ' + error, 'Error');
                }
            });
        }
    }


    getIcon(state: boolean) {
        return state ? 'done' : 'close';
    }

    getClass(state: boolean) {
        return state ? 'primary' : 'warn';
    }
}
