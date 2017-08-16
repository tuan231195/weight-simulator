import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {VehicleType} from '../model/vehicle-type';
import {Vehicle} from '../model/vehicle';
@Injectable()
export class VehicleService
{
    constructor(private http:Http){}

    getVehicleTypes() {
        return this.http.get(`/api/vtypes`).map(response => response.json());
    }

    findVehicleTypes(vehicleType: VehicleType) {
        return this.http.get(`/api/vehicle/${vehicleType.code}`).map(response => response.json());
    }

    deleteVehicle(selectedVehicle: Vehicle) {
        return this.http.delete(`/api/vehicle/${selectedVehicle.code}`);
    }

    createVehicle(selectedVehicle: Vehicle) {
        return this.http.post(`/api/vehicle/`, selectedVehicle).map(response => response.json());
    }

    saveVehicle(selectedVehicle: Vehicle) {
        return this.http.put(`/api/vehicle`, selectedVehicle).map(response => response.json());
    }
}