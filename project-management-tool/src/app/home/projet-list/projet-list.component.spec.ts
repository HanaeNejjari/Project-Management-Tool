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

describe('ProjetListComponent', () => {
  let component: ProjetListComponent;
  let fixture: ComponentFixture<ProjetListComponent>;

  beforeEach(async () => {
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
      declarations: [ProjetListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjetListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
