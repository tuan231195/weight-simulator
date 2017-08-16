import {Component, forwardRef, Input} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
export const NUMBER_INPUT_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => NumberInputComponent),
    multi: true
};

@Component({
    selector: 'vr-number-input',
    templateUrl: './number-input.component.html',
    providers: [
        NUMBER_INPUT_VALUE_ACCESSOR
    ]
})
export class NumberInputComponent implements ControlValueAccessor {
    propagateChange = (_: any) => {
    };
    private innerValue: number = 0;

    @Input()
    placeholder: String;

    @Input()
    maxlength: number;

    get value() {
        return this.innerValue;
    }

    set value(v: any) {
        this.innerValue = v;
        this.propagateChange(v);
    }

    writeValue(v: any): void {
        this.value = v;
    }

    registerOnChange(fn: any): void {
        this.propagateChange = fn;
    }

    registerOnTouched(fn: any): void {
    }


}