import { TestBed } from '@angular/core/testing';

import { ProjetService } from './projet.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Projet} from '../models/projet';
import {environment} from '../../environments/environment';

describe('ProjetService', () => {
  let service: ProjetService;
  let httpMock: HttpTestingController;

  const mockProjet: Projet = {
    id: 1,
    nom: 'Projet Test',
    projetDesc: 'Description',
    dateDebut: new Date()
  };

  beforeEach(() => {
    localStorage.setItem('token', 'mockToken');
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProjetService]
    });
    service = TestBed.inject(ProjetService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get user projects', () => {
    service.getUserProjets().subscribe(projets => {
      expect(projets.length).toBe(1);
      expect(projets[0].nom).toBe('Projet Test');
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/projets/mesprojets`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe('Bearer mockToken');
    req.flush([mockProjet]);
  });

  it('should create a new project', () => {
    service.createProjet(mockProjet).subscribe(projet => {
      expect(projet.nom).toBe('Projet Test');
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/projets`);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Authorization')).toBe('Bearer mockToken');
    req.flush(mockProjet);
  });

  it('should update a project', () => {
    service.updateProjet(mockProjet).subscribe(projet => {
      expect(projet.nom).toBe('Projet Test');
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/projets/${mockProjet.id}`);
    expect(req.request.method).toBe('PUT');
    req.flush(mockProjet);
  });

  it('should delete a project', () => {
    service.deleteProjet(mockProjet).subscribe(response => {
      expect(response).toBe('Projet supprimé');
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/projets/${mockProjet.id}`);
    expect(req.request.method).toBe('DELETE');
    req.flush('Projet supprimé');
  });

  it('should get user role in project', () => {
    service.getUserRole(1, 2).subscribe(role => {
      expect(role).toBe('Administrateur');
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/projets/role/1/2`);
    expect(req.request.method).toBe('GET');
    expect(req.request.responseType).toBe('text');
    req.flush('Administrateur');
  });

  it('should get users in project', () => {
    service.getUsersInprojet(1).subscribe(users => {
      expect(users.length).toBeGreaterThanOrEqual(0);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/projets/1/users`);
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  it('should add a user to a project with role', () => {
    service.addUserInProjet('test@mail.com', 1, 'Membre').subscribe(res => {
      expect(res).toBeTruthy();
    });

    const req = httpMock.expectOne(r =>
      r.method === 'POST' &&
      r.url === `${environment.apiUrl}/projets/assignrole` &&
      r.params.get('addEmail') === 'test@mail.com' &&
      r.params.get('projetId') === '1' &&
      r.params.get('role') === 'Membre'
    );
    expect(req.request.headers.get('Authorization')).toBe('Bearer mockToken');
    req.flush({ success: true });
  });

  it('should update user role', () => {
    service.changeUserRole('test@mail.com', 1, 'Administrateur').subscribe(res => {
      expect(res).toBeTruthy();
    });

    const req = httpMock.expectOne(r =>
      r.method === 'PUT' &&
      r.url === `${environment.apiUrl}/projets/updaterole` &&
      r.params.get('updateEmail') === 'test@mail.com' &&
      r.params.get('projetId') === '1' &&
      r.params.get('role') === 'Administrateur'
    );
    req.flush({ success: true });
  });

  it('should delete user role', () => {
    service.deleteUserRole(42).subscribe(res => {
      expect(res).toBe('ok');
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/projets/removerole/42`);
    expect(req.request.method).toBe('DELETE');
    expect(req.request.headers.get('Authorization')).toBe('Bearer mockToken');
    req.flush('ok');
  });

});
