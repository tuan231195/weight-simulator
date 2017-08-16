import {Http} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {PlateConfig} from '../model/plate-config';
import {VehicleType} from '../model/vehicle-type';
import * as uuid from 'uuid';
import {SimRequest} from '../model/sim-request';
import {SimState} from '../model/sim-state';
import {Injectable} from '@angular/core';
import 'rxjs/add/operator/do';
import 'rxjs/add/observable/throw'


@Injectable()
export class WeightService {
    private sessionId: string;

    constructor(private http: Http) {
    }

    getAllPlateConfigs(): Observable<PlateConfig[]> {
        return this.http.get('/api/plates').map(response => response.json());
    }

    getAllVtypes(plateNum: number): Observable<VehicleType[]> {
        return this.http.get(`/api/plates/${plateNum}/vtypes`).map(response => response.json());
    }

    simulate(plateConfig: PlateConfig, vType: string): Observable<any> {
        let simRequest: SimRequest = new SimRequest();
        simRequest.sessionId = uuid();
        simRequest.numPlate = plateConfig.num;
        simRequest.version = plateConfig.version;
        simRequest.code = vType;
        return this.http.post('/api/simulate', simRequest).do(response => {
            this.sessionId = simRequest.sessionId;
        });
    }

    stop(): Observable<any> {
        if (this.sessionId) {
            let simRequest: SimRequest = new SimRequest();
            simRequest.sessionId = this.sessionId;
            return this.http.post('/api/stop', simRequest);
        }
        return Observable.throw('No session id exists');
    }

    getState(): Observable<SimState> {
        if (this.sessionId) {
            return this.http.get(`/api/state/${this.sessionId}`).map(response => response.json()).do((state: SimState) => {
                if (state.stopped) {
                    this.sessionId = null;
                }
            });
        }
        return Observable.throw('No session id exists');
    }

    nextStep(): Observable<any> {
        if (this.sessionId) {
            let simRequest: SimRequest = new SimRequest();
            simRequest.sessionId = this.sessionId;
            return this.http.post('/api/next', simRequest);
        }
        return Observable.throw('No session id exists');
    }

}
