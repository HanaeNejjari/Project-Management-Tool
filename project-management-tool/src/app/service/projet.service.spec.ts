import { TestBed } from '@angular/core/testing';

import { ProjetService } from './projet.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('ProjetService', () => {
  let service: ProjetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(ProjetService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
