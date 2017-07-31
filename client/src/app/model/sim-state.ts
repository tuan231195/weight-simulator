import {VehicleState} from './vehicle-state';
export class SimState {
    vehicleStates: VehicleState[];
    currentVehiclePosition: number;
    numSteps: number;
    plate: number;
    step: number;
    stage: number;
}