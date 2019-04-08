import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'app-event-value-model',
    templateUrl: './event-value-model.component.html',
    styleUrls: ['./event-value-model.component.css']
})
export class EventValueModelComponent implements OnInit {
    @Input() eventValueJson;

    public eventValue: JSON;
    constructor(public activeModal: NgbActiveModal) {}

    ngOnInit() {
        this.eventValue = this.eventValueJson;
    }
}
