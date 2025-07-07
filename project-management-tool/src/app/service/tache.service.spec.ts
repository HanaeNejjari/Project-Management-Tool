import { TestBed } from '@angular/core/testing';

import { TacheService } from './tache.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('TacheService', () => {
  let service: TacheService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(TacheService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
