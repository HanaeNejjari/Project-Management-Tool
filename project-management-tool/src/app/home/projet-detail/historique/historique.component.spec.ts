import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoriqueComponent } from './historique.component';
import {UserService} from '../../../service/user.service';
import {Router} from '@angular/router';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from '../../../app-routing.module';
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
import {HistoriqueService} from '../../../service/historique.service';
import {of} from 'rxjs';

describe('HistoriqueComponent', () => {
  let component: HistoriqueComponent;
  let fixture: ComponentFixture<HistoriqueComponent>;
  let mockService: jasmine.SpyObj<HistoriqueService>;

  const mockDialogData = { tacheId: 123 };

  beforeEach(async () => {
    mockService = jasmine.createSpyObj('HistoriqueService', ['getHistoByTache']);

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
      declarations: [HistoriqueComponent],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: mockService },
        { provide: MatDialogRef, useValue: mockDialogData }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HistoriqueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


});
