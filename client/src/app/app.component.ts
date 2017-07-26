import {Component, OnDestroy} from '@angular/core';

import '../assets/css/styles.css';
import {StompService} from './websocket.service';
import {Subscription} from 'rxjs/Subscription';

@Component({
    selector: 'my-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent{
}
