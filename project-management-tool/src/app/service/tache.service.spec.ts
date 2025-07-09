import { TestBed } from '@angular/core/testing';

import { TacheService } from './tache.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Tache} from '../models/tache';
import {environment} from '../../environments/environment';

describe('TacheService', () => {
  let service: TacheService;
  let httpMock: HttpTestingController;

  const mockTache: Tache = {
    id: 1,
    nom: 'Tâche test',
    tacheDesc: 'Description test',
    dateEcheance: new Date(),
    priorite: 'Haute',
    dateFin: new Date(),
    statut: 'Non commencé',
    idProjet: 1,
    idUtilisateur: 2
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TacheService]
    });
    service = TestBed.inject(TacheService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get tasks by project', () => {
    service.getTachesByProjet(1).subscribe(taches => {
      expect(taches.length).toBe(1);
      expect(taches[0].nom).toBe('Tâche test');
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/taches/projet/1`);
    expect(req.request.method).toBe('GET');
    req.flush([mockTache]);
  });

  it('should add a task', () => {
    const tacheToAdd: Partial<Tache> = {
      nom: 'Nouvelle tâche',
      priorite: 'Basse',
      statut: 'Non commencé'
    };

    service.addTache(tacheToAdd, 1, 2).subscribe(tache => {
      expect(tache.nom).toBe('Tâche test');
    });

    const req = httpMock.expectOne(r =>
      r.method === 'POST' &&
      r.url === `${environment.apiUrl}/taches` &&
      r.params.get('projetId') === '1' &&
      r.params.get('userId') === '2'
    );
    req.flush(mockTache);
  });

  it('should update a task', () => {
    const updatedTache: Partial<Tache> = {
      ...mockTache,
      nom: 'Tâche mise à jour'
    };

    service.updateTache(updatedTache).subscribe(tache => {
      expect(tache.nom).toBe('Tâche mise à jour');
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/taches`);
    expect(req.request.method).toBe('PUT');
    req.flush(updatedTache);
  });

  it('should assign a task to user', () => {
    service.assignTache(1, 2).subscribe(tache => {
      expect(tache.id).toBe(1);
      expect(tache.idUtilisateur).toBe(2);
    });

    const req = httpMock.expectOne(r =>
      r.method === 'POST' &&
      r.url === `${environment.apiUrl}/taches/assignTask` &&
      r.params.get('tacheId') === '1' &&
      r.params.get('userId') === '2'
    );
    req.flush(mockTache);
  });

});
