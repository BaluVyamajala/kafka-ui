import { TestBed, inject } from '@angular/core/testing';

import { ModelsService } from './models.service';

describe('ModelsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ModelsService]
    });
  });

  it('should be created', inject([ModelsService], (service: ModelsService) => {
    expect(service).toBeTruthy();
  }));
});
