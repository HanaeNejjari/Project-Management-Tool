import { TestBed } from '@angular/core/testing';

import { HistoriqueService } from './historique.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {environment} from '../../environments/environment';

describe('HistoriqueService', () => {
  let service: HistoriqueService;
  let httpMock: HttpTestingController;

  const mockHisto = [
    { id: 1, champModifie: 'statut', ancienneValeur: 'Non commencé', nouvelleValeur: 'En cours', dateModification: '2025-07-06' },
    { id: 2, champModifie: 'priorite', ancienneValeur: 'Basse', nouvelleValeur: 'Haute', dateModification: '2025-07-07' }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [HistoriqueService]
    });
    service = TestBed.inject(HistoriqueService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch historique by tache ID', () => {
    const tacheId = 1;

    service.getHistoByTache(tacheId).subscribe(histo => {
      expect(histo).toEqual(mockHisto);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/histo/tache/${tacheId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockHisto);
  });

});
