import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjetDetailComponent } from './projet-detail.component';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from '../../app-routing.module';
import {MatButton, MatFabButton, MatIconButton, MatMiniFabButton} from '@angular/material/button';
import {MatCard, MatCardContent, MatCardHeader, MatCardModule, MatCardTitle} from '@angular/material/card';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatIcon} from '@angular/material/icon';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from '@angular/common/http';
import {MatToolbar} from '@angular/material/toolbar';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav';
import {MatList, MatListItem, MatNavList} from '@angular/material/list';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {MatDividerModule} from '@angular/material/divider';
import {MatChipsModule} from '@angular/material/chips';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule} from '@angular/material/core';
import {MatSelectModule} from '@angular/material/select';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatDialogModule} from '@angular/material/dialog';
import {ProjetService} from '../../service/projet.service';
import {TacheService} from '../../service/tache.service';
import {Projet} from '../../models/projet';
import {User} from '../../models/user';
import {of, throwError} from 'rxjs';

describe('ProjetDetailComponent', () => {
  let component: ProjetDetailComponent;
  let fixture: ComponentFixture<ProjetDetailComponent>;
  let projetServiceSpy: jasmine.SpyObj<ProjetService>;
  let tacheServiceSpy: jasmine.SpyObj<TacheService>;
  let snackBarSpy: jasmine.SpyObj<MatSnackBar>;

  const mockUser: User = { id: 1, email: 'test@mail.com', motDePasse: '', nomUtilisateur: '' };
  const mockProjet: Projet = { id: 1, nom: 'Projet Test', projetDesc: '', dateDebut: new Date() };
  const mockTache: any = {
    id: 1,
    nom: 'Tache Test',
    tacheDesc: '',
    dateEcheance: new Date(),
    priorite: 'Moyenne',
    dateFin: new Date(),
    statut: 'En cours',
    user: { id: 1 },
    projet: { id: 1 },
  };

  beforeEach(async () => {
    const projetSpy = jasmine.createSpyObj('ProjetService', [
      'getUserRole', 'getUsersInprojet', 'updateProjet', 'addUserInProjet',
      'changeUserRole', 'deleteUserRole'
    ]);
    const tacheSpy = jasmine.createSpyObj('TacheService', [
      'getTachesByProjet', 'addTache', 'updateTache', 'assignTache'
    ]);
    snackBarSpy = jasmine.createSpyObj('MatSnackBar', ['open'])

    await TestBed.configureTestingModule({
      imports: [
        BrowserModule,
        AppRoutingModule,
        MatButton,
        MatCard,
        MatCardTitle,
        MatCardContent,
        MatInputModule,
        MatFormFieldModule,
        ReactiveFormsModule,
        MatIcon,
        MatIconButton,
        BrowserAnimationsModule,
        HttpClientModule,
        MatToolbar,
        MatSidenav,
        MatSidenavContainer,
        MatSidenavContent,
        MatNavList,
        MatListItem,
        MatList,
        FormsModule,
        MatSnackBarModule,
        MatCardHeader,
        MatCardModule,
        MatDividerModule,
        MatChipsModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatFabButton,
        MatMiniFabButton,
        MatSelectModule,
        MatExpansionModule,
        MatDialogModule,
      ],
      declarations: [ProjetDetailComponent],
      providers: [
        { provide: ProjetService, useValue: projetSpy },
        { provide: TacheService, useValue: tacheSpy },
        { provide: MatSnackBar, useValue: snackBarSpy },
      ]
    })
    .compileComponents();

    projetServiceSpy = TestBed.inject(ProjetService) as jasmine.SpyObj<ProjetService>;
    tacheServiceSpy = TestBed.inject(TacheService) as jasmine.SpyObj<TacheService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjetDetailComponent);
    component = fixture.componentInstance;
    component.user = mockUser;
    component.projet = mockProjet;

    projetServiceSpy.getUserRole.and.returnValue(of('Administrateur'));
    projetServiceSpy.getUsersInprojet.and.returnValue(of([
      {
        id: 1,
        libelle: 'Administrateur',
        utilisateur: { id: 1, email: 'test@mail.com' }
      }
    ]));
    tacheServiceSpy.getTachesByProjet.and.returnValue(of([mockTache]));

    fixture.detectChanges();
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user role, users and taches on init', () => {
    expect(projetServiceSpy.getUserRole).toHaveBeenCalledWith(1, 1);
    expect(projetServiceSpy.getUsersInprojet).toHaveBeenCalledWith(1);
    expect(tacheServiceSpy.getTachesByProjet).toHaveBeenCalledWith(1);
  });

  it('should start edit', () => {
    component.startEdit();
    expect(component.isEditing).toBeTrue();
    expect(component.updatedProjet).toEqual(mockProjet);
    expect(component.editedRoles['test@mail.com']).toBe('Administrateur');
  });

  it('should add user in projet', () => {
    projetServiceSpy.addUserInProjet.and.returnValue(of({}));
    component.newEmail = 'new@mail.com';
    component.newRole = 'Membre';
    component.addUserInProjet();
    expect(projetServiceSpy.addUserInProjet).toHaveBeenCalledWith('new@mail.com', 1, 'Membre');
  });

  it('should change user role', () => {
    projetServiceSpy.changeUserRole.and.returnValue(of({}));
    component.editedRoles['test@mail.com'] = 'Membre';
    component.changeRole('test@mail.com');
    expect(projetServiceSpy.changeUserRole).toHaveBeenCalledWith('test@mail.com', 1, 'Membre');
  });

  it('should delete user in projet', () => {
    projetServiceSpy.deleteUserRole.and.returnValue(of('OK'));
    component.deleteUserInProjet('test@mail.com');
    expect(projetServiceSpy.deleteUserRole).toHaveBeenCalledWith(1);
  });

  it('should create tache if user is in projet', () => {
    tacheServiceSpy.addTache.and.returnValue(of(mockTache));
    component.createTache();
    expect(tacheServiceSpy.addTache).toHaveBeenCalled();
  });

  it('should start edit tache', () => {
    component.startEditTache(mockTache);
    expect(component.editedTacheId).toBe(mockTache.id);
    expect(component.editedTache.nom).toBe(mockTache.nom);
  });

  it('should save tache', () => {
    tacheServiceSpy.updateTache.and.returnValue(of(mockTache));
    component.editedTacheId = 1;
    component.editedTache = { ...mockTache };
    component.saveTache();
    expect(tacheServiceSpy.updateTache).toHaveBeenCalledWith(mockTache);
  });

  it('should assign tache', () => {
    tacheServiceSpy.assignTache.and.returnValue(of(mockTache));
    component.editedTacheId = 1;
    component.editedTache = { idUtilisateur: 1 };
    component.assignTache();
    expect(tacheServiceSpy.assignTache).toHaveBeenCalledWith(1, 1);
  });

  it('should cancel edit tache', () => {
    component.editedTacheId = 1;
    component.cancelEditTache();
    expect(component.editedTacheId).toBeNull();
  });

  it('should get user email', () => {
    const email = component.getUserEmail(1);
    expect(email).toBe('test@mail.com');
  });

  it('should return \'Inconnu\' if user not found', () => {
    const email = component.getUserEmail(999);
    expect(email).toBe('Inconnu');
  });

  it('should save edited projet and emit change on success', () => {
    component.updatedProjet = mockProjet;
    projetServiceSpy.updateProjet.and.returnValue(of(mockProjet));

    component.saveEdit();

    expect(projetServiceSpy.updateProjet).toHaveBeenCalledWith(mockProjet);
    expect(component.projet).toEqual(mockProjet);
    expect(component.isEditing).toBeFalse();
  });

  it('should show error message if saveEdit fails', () => {
    const errorResponse = { error: { message: 'Erreur serveur' } };
    component.updatedProjet = mockProjet
    projetServiceSpy.updateProjet.and.returnValue(throwError(() => errorResponse));

    component.saveEdit();

    expect(snackBarSpy.open).toHaveBeenCalledWith('Erreur serveur', 'OK', {
      duration: 2000,
      panelClass: ['error-snackbar']
    });
  });

  it('should show error message if addUserInProjet fails', () => {
    component.newEmail = 'fail@mail.com';
    component.newRole = 'Membre';
    component.projet = mockProjet;
    const errorResponse = {error: {message: 'Erreur ajout'}};

    projetServiceSpy.addUserInProjet.and.returnValue(throwError(() => errorResponse));

    component.addUserInProjet();

    expect(projetServiceSpy.addUserInProjet).toHaveBeenCalledWith('fail@mail.com', mockProjet.id, 'Membre');
    expect(snackBarSpy.open).toHaveBeenCalledWith('Erreur ajout', 'OK', {
      duration: 2000,
      panelClass: ['error-snackbar']
    });
  });

  it('should show error message if changeRole fails', () => {
    component.projet = mockProjet;
    component.editedRoles = { 'test@mail.com': 'Membre' };
    const errorResponse = { error: { message: 'Erreur changement rôle' } };
    projetServiceSpy.changeUserRole.and.returnValue(throwError(() => errorResponse));

    component.changeRole('test@mail.com');

    expect(projetServiceSpy.changeUserRole).toHaveBeenCalledWith('test@mail.com', mockProjet.id, 'Membre');
    expect(snackBarSpy.open).toHaveBeenCalledWith('Erreur changement rôle', 'OK', {
      duration: 2000,
      panelClass: ['error-snackbar']
    });
  });

  it('should show error message if deleteUserInProjet fails', () => {
    component.projet = mockProjet;
    component.usersInProjet = [
      { id: 1, email: 'delete@mail.com', role: 'Membre', userId: 1 }
    ];
    const errorResponse = { error: { message: 'Erreur suppression rôle' } };
    projetServiceSpy.deleteUserRole.and.returnValue(throwError(() => errorResponse));

    component.deleteUserInProjet('delete@mail.com');

    expect(projetServiceSpy.deleteUserRole).toHaveBeenCalledWith(1);
    expect(snackBarSpy.open).toHaveBeenCalledWith('Erreur suppression rôle', 'OK', {
      duration: 2000,
      panelClass: ['error-snackbar']
    });
  });

  it('should log error if getTaches fails', () => {
    component.projet = mockProjet;
    const consoleSpy = spyOn(console, 'error');
    const errorResponse = { error: { message: 'Erreur récupération tâches' } };

    tacheServiceSpy.getTachesByProjet.and.returnValue(throwError(() => errorResponse));

    component.getTaches();

    expect(tacheServiceSpy.getTachesByProjet).toHaveBeenCalledWith(mockProjet.id);
    expect(consoleSpy).toHaveBeenCalledWith('Erreur recupération taches', errorResponse);
  });

  it('should cancel editing projet', () => {
    component.isEditing = true;
    component.cancelEdit();
    expect(component.isEditing).toBeFalse();
  });


});
