import { TestBed, inject } from '@angular/core/testing';

import { ConsumerGroupService } from './consumer-group.service';

describe('ConsumerGroupService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ConsumerGroupService]
    });
  });

  it('should be created', inject([ConsumerGroupService], (service: ConsumerGroupService) => {
    expect(service).toBeTruthy();
  }));
});
