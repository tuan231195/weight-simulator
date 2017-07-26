import {TestBed} from '@angular/core/testing';

import {SettingsComponent} from './settings.component';
describe('App', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [SettingsComponent]
        });
    });

    it('should work', () => {
        let fixture = TestBed.createComponent(SettingsComponent);
        expect(fixture.componentInstance instanceof SettingsComponent).toBe(true, 'should create AppComponent');
    });
});
