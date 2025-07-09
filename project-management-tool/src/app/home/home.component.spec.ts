import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeComponent } from './home.component';
import {MatButton, MatFabButton, MatIconButton, MatMiniFabButton} from '@angular/material/button';
import {MatCard, MatCardContent, MatCardHeader, MatCardModule, MatCardTitle} from '@angular/material/card';
import {MatInput, MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIcon} from '@angular/material/icon';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from '../app-routing.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
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
import {ProjetListComponent} from './projet-list/projet-list.component';
import {ProjetDetailComponent} from './projet-detail/projet-detail.component';
import {UserService} from '../service/user.service';
import {Router} from '@angular/router';
import {User} from '../models/user';
import {Projet} from '../models/projet';
import {of} from 'rxjs';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;
  let userServiceSpy: jasmine.SpyObj<UserService>;
  let routerSpy: jasmine.SpyObj<Router>;

  const mockUser: User = {
    id: 1,
    email: 'test@mail.com',
    nomUtilisateur: 'TestUser',
    motDePasse: 'password'
  };

  const mockProjet: Projet = {
    id: 1,
    nom: 'Projet Test',
    projetDesc: 'Description',
    dateDebut: new Date()
  };

  beforeEach(async () => {
    userServiceSpy = jasmine.createSpyObj('UserService', ['getCurrentUserInfo', 'logout']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [HomeComponent, ProjetListComponent, ProjetDetailComponent],
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
      providers: [
        { provide: UserService, useValue: userServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    userServiceSpy.getCurrentUserInfo.and.returnValue(of(mockUser));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user info on init', () => {
    expect(userServiceSpy.getCurrentUserInfo).toHaveBeenCalled();
    expect(component.userInfo).toEqual(mockUser);
  });

  it('should select a project', () => {
    component.onProjetSelected(mockProjet);
    expect(component.selectedProjet).toEqual(mockProjet);
  });

  it('should logout and navigate', () => {
    component.logout();
    expect(userServiceSpy.logout).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/auth/choice']);
  });

  it('should update project in list and selection', () => {
    const updatedProjet = { ...mockProjet, nom: 'Projet MAJ' };
    component.projets = [mockProjet];
    component.onProjetChanged(updatedProjet);
    expect(component.projets[0].nom).toBe('Projet MAJ');
    expect(component.selectedProjet).toEqual(updatedProjet);
  });

});
