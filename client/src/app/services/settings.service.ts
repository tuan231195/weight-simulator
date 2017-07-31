import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {Settings} from '../model/settings';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class SettingService {
    constructor(private http: Http) {
    }

    getAll(): Observable<Settings> {
        return this.http.get('/api/settings').map(response => response.json());
    }
}