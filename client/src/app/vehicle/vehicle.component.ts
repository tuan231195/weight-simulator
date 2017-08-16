import {Component, Input, OnInit} from '@angular/core';
import {Settings} from '../model/settings';
import {VehicleType} from '../model/vehicle-type';
import {VehicleService} from '../services/vehicle.service';
import {Subscription} from 'rxjs/Subscription';
import {Vehicle} from '../model/vehicle';
import {ToastsManager} from 'ng2-toastr';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
@Component({
    selector: 'vr-vehicle',
    templateUrl: './vehicle.component.html',
    styleUrls: ['./vehicle.component.css']
})
export class VehicleComponent implements OnInit {
    @Input() settings: Settings;

    selectedVehicleType: VehicleType;
    vehicleTypes: VehicleType[];
    selectedVehicle: Vehicle;
    vehicles: Vehicle[];
    create: boolean;

    vehicleTypeSubscription: Subscription;
    vehicleSubscription: Subscription;
    deleteSubscription: Subscription;
    createSubscription: Subscription;
    saveSubscription: Subscription;

    vehicleForm: FormGroup;

    constructor(private vehicleService: VehicleService, private toastr: ToastsManager, private formBuilder: FormBuilder) {

    }

    ngOnInit() {
        this.vehicleForm = this.formBuilder.group({
            'id': new FormControl({value: '', disabled: true}),
            'name': new FormControl('', [Validators.required]),
            'axle1': new FormControl(0, [Validators.required]),
            'axle2': new FormControl(0, [Validators.required]),
            'axle3': new FormControl(0, [Validators.required]),
            'axle4': new FormControl(0, [Validators.required]),
            'axle5': new FormControl(0, [Validators.required]),
        });
        this.vehicleTypeSubscription = this.vehicleService.getVehicleTypes().subscribe((vehicleTypes: VehicleType[]) => {
            this.vehicleTypes = vehicleTypes;
            this.vehicleTypeSubscription.unsubscribe();
        }, (error) => {
            this.toastr.error('Failed to get vehicle types', 'Error');
            this.vehicleTypeSubscription.unsubscribe();
        });
    }


    selectVehicleType() {
        this.selectedVehicle = null;
        let vehicleType = this.selectedVehicleType;
        this.loadVehicles(vehicleType);
        this.create = false;
    }

    selectVehicle(vehicle: Vehicle) {
        this.selectedVehicle = Object.assign({}, vehicle);
        this.create = false;
        let index = vehicle.code.indexOf('-');
        if (index != -1) {
            this.selectedVehicle.id = parseInt(vehicle.code.substr(index + 1));
        }
        else {
            this.selectedVehicle.id = 0;
        }
    }

    deleteVehicle() {
        this.create = false;
        this.deleteSubscription = this.vehicleService.deleteVehicle(this.selectedVehicle).subscribe(() => {
            this.loadVehicles(this.selectedVehicleType);
            this.deleteSubscription.unsubscribe();
            this.selectedVehicle = null;
        }, () => {
            this.deleteSubscription.unsubscribe();
        });
    }

    newVehicle() {
        this.selectedVehicle = new Vehicle();
        this.selectedVehicle.type = this.selectedVehicleType.code;
        this.selectedVehicle.axle1 = this.selectedVehicleType.axle1;
        this.selectedVehicle.axle2 = this.selectedVehicleType.axle2;
        this.selectedVehicle.axle3 = this.selectedVehicleType.axle3;
        this.selectedVehicle.axle4 = this.selectedVehicleType.axle4;
        this.selectedVehicle.axle5 = this.selectedVehicleType.axle5;
        this.create = true;
    }

    createOrSave() {
        if (this.create) {
            this.createSubscription = this.vehicleService.createVehicle(this.selectedVehicle).subscribe((vehicle: Vehicle) => {
                this.selectedVehicle = vehicle;
                this.loadVehicles(this.selectedVehicleType);
                this.create = false;
            }, (error) => {
                this.toastr.error('Failed to create new vehicle', 'Error');
            });
        }
        else {
            this.saveSubscription = this.vehicleService.saveVehicle(this.selectedVehicle).subscribe((vehicle: Vehicle) => {
                this.selectedVehicle = vehicle;
                this.loadVehicles(this.selectedVehicleType);
            }, (error) => {
                this.toastr.error('Failed to save vehicle', 'Error');
            })
        }

    }

    loadVehicles(vehicleType: VehicleType) {
        this.vehicleSubscription = this.vehicleService.findVehicleTypes(vehicleType).subscribe((vehicles: Vehicle[]) => {
            this.vehicles = vehicles;
            this.vehicleSubscription.unsubscribe();
        }, (error) => {
            this.toastr.error(`Failed to get vehicles for type ${vehicleType.code}`, 'Error');
            this.vehicleSubscription.unsubscribe();
        })
    }
}