import {Injectable} from '@angular/core';
import {Subject} from 'rxjs/Subject';
import {Observable} from 'rxjs/Observable';
import {Logger} from 'angular2-logger/core';
import {Subscriber} from 'rxjs/Subscriber';
import {Client, Frame, Message} from 'stompjs';
import * as Stomp from 'stompjs';

@Injectable()
export class StompService {
    constructor(private logger: Logger) {
    }

    private stompClient: Client;
    private connected = false;
    private stompSubject: Subject<any> = new Subject<any>();

    public connect(url: string): Observable<boolean> {
        let webSocket = new WebSocket(url);
        this.stompClient = Stomp.over(webSocket);
        return Observable.create((subscriber: Subscriber<boolean>) => {
            this.stompClient.connect({login: '', passcode: ''}, (frame: Frame) => {
                this.connected = true;
                this.logger.debug('Successfully connected to websocket');
                subscriber.next();
            }, (error: string) => {
                this.logger.error('Failed to connect to websocket. Reason: ' + error);
                subscriber.error();
            });

            return () => {
                this.stompClient.disconnect(() => {
                    this.logger.debug('Disconnected from websocket');
                });
            };
        });

    }

    public send(destination: string, payload: string) {
        if (!this.connected) {
            this.logger.warn('Websock is not connected');
            return;
        }
        this.stompClient.send(destination, {}, JSON.stringify({'data': payload}));
    }

    public listen(destination: string): Observable<any> {
        this.stompClient.subscribe(destination, (message: Message) => {
            let result;
            try {
                result = JSON.parse(message.body);
            }
            catch (exception) {
                result = message.body;
            }
            this.stompSubject.next(result);
        });

        return this.stompSubject.asObservable();
    }
}