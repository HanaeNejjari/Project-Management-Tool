import { TestBed } from '@angular/core/testing';

import { HistoriqueService } from './historique.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('HistoriqueService', () => {
  let service: HistoriqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(HistoriqueService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
