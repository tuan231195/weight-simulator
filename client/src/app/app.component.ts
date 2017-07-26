import {Component, OnDestroy} from '@angular/core';
import {StompService} from './websocket.service';
import {Subscription} from 'rxjs/Subscription';
import '../assets/css/styles.css';
@Component({
    selector: 'vr-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
}
