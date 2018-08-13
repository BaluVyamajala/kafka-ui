import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EventValueModelComponent } from './event-value-model.component';

describe('EventValueModelComponent', () => {
  let component: EventValueModelComponent;
  let fixture: ComponentFixture<EventValueModelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EventValueModelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EventValueModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
