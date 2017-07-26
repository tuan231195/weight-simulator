import {Component, OnDestroy, OnInit} from '@angular/core';
import {Plate} from '../model/plate';
import {Http} from '@angular/http';
import {Subscription} from 'rxjs/Subscription';


@Component({
    selector: 'vr-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy{

    selectedPlate: number;
    selected: boolean = false;
    selectedVehicle: number;
    isRunning: boolean = false;
    plates: Plate[];
    plateSubscription: Subscription;

    constructor(private http:Http) {
        this.plates = [
            {plateNum: 1, scaleActive: true, scaleJoin: false, weight: 4.5, port: 2002},
            {plateNum: 2, scaleActive: false, scaleJoin: true, weight: 5, port: 2003},
            {plateNum: 3, scaleActive: true, scaleJoin: false, weight: 6, port: 2004},
            {plateNum: 4, scaleActive: true, scaleJoin: true, weight: 2.5, port: 2005}
        ];
    }

    ngOnInit(): void {
        this.plateSubscription = this.http.get('/api/plates').subscribe((e) => {
            console.log(e);
        });
    }

    ngOnDestroy(): void {
        this.plateSubscription.unsubscribe();
    }


    select() {
        setTimeout(() => {
            this.selected = true;
        }, 200);

    }

    unselect() {
        setTimeout(() => {
            this.selected = false;
        }, 200);
    }

    run(){
        this.isRunning = true;
    }

    next(){
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
