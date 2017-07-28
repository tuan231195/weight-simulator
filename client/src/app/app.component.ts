import {Component, OnDestroy} from '@angular/core';
import {StompService} from './websocket.service';
import {Subscription} from 'rxjs/Subscription';
@Component({
    selector: 'vr-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnDestroy {
    ngOnDestroy(): void {
        if (this.connectSubscription) {
            this.connectSubscription.unsubscribe();
        }
        if (this.messageSubscription) {
            this.messageSubscription.unsubscribe();
        }
    }

    private connectSubscription: Subscription;
    private messageSubscription: Subscription;

    constructor(private stompService: StompService) {
        this.connectSubscription = stompService.connect('ws://localhost:8081/ws-weightsim').subscribe(() => {
            this.messageSubscription = stompService.listen('/topic/test').subscribe((value) => {
                console.log(value);
            });
            stompService.send('/ws/state', 'test');
        });
    }
}