import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjetListComponent } from './projet-list.component';
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
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatDividerModule} from '@angular/material/divider';
import {MatChipsModule} from '@angular/material/chips';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule} from '@angular/material/core';
import {MatSelectModule} from '@angular/material/select';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatDialogModule} from '@angular/material/dialog';
import {ProjetService} from '../../service/projet.service';
import {User} from '../../models/user';
import {Projet} from '../../models/projet';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {of, throwError} from 'rxjs';

describe('ProjetListComponent', () => {
  let component: ProjetListComponent;
  let fixture: ComponentFixture<ProjetListComponent>;
  let mockProjetService: jasmine.SpyObj<ProjetService>;

  const mockProjets: Projet[] = [
    { id: 1, nom: 'Projet 1', projetDesc: 'Description', dateDebut: new Date() },
    { id: 2, nom: 'Projet 2', projetDesc: 'Description 2', dateDebut: new Date() },
  ];


  beforeEach(async () => {
    mockProjetService = jasmine.createSpyObj('ProjetService', ['getUserProjets', 'createProjet']);

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
      declarations: [ProjetListComponent],
      providers: [
        { provide: ProjetService, useValue: mockProjetService },
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]

    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjetListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjetListComponent);
    component = fixture.componentInstance;
    component.user = { id: 1, email: 'test@mail.com', nomUtilisateur: 'TestUser' } as User;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get projects on init', () => {
    mockProjetService.getUserProjets.and.returnValue(of(mockProjets));
    component.ngOnInit();
    expect(mockProjetService.getUserProjets).toHaveBeenCalled();
    expect(component.projets.length).toBe(2);
  });

  it('should handle error when getUserProjets fails', () => {
    spyOn(console, 'error');
    mockProjetService.getUserProjets.and.returnValue(throwError(() => new Error('error')));
    component.ngOnInit();
    expect(console.error).toHaveBeenCalled();
  });

  it('should emit selected project', () => {
    spyOn(component.projetSelected, 'emit');
    const projet = mockProjets[0];
    component.selectProjet(projet);
    expect(component.projetSelected.emit).toHaveBeenCalledWith(projet);
  });

  it('should create a new project and select it', () => {
    const newProjet = { id: 3, nom: 'Nouveau Projet', projetDesc: 'Description projet', dateDebut: new Date() };
    mockProjetService.createProjet.and.returnValue(of(newProjet));
    mockProjetService.getUserProjets.and.returnValue(of([...mockProjets, newProjet]));

    spyOn(component, 'selectProjet');
    component.createProjet();

    expect(component.selectProjet).toHaveBeenCalledWith(newProjet);
    expect(mockProjetService.createProjet).toHaveBeenCalled();
    expect(mockProjetService.getUserProjets).toHaveBeenCalled();
  });


});
