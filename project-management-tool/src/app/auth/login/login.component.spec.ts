import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { LoginComponent } from './login.component';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatInput} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatIcon} from '@angular/material/icon';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {UserService} from '../../service/user.service';
import {Router} from '@angular/router';
import {of, throwError} from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let userServiceSpy: jasmine.SpyObj<UserService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const userServiceMock = jasmine.createSpyObj('UserService', ['login', 'setToken']);
    const routerMock = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        MatButton,
        MatCard,
        MatCardTitle,
        MatCardContent,
        MatInput,
        MatFormFieldModule,
        ReactiveFormsModule,
        MatIcon,
        MatIconButton,
        BrowserAnimationsModule,
        FormsModule,
        HttpClientTestingModule,
      ],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    userServiceSpy = TestBed.inject(UserService) as jasmine.SpyObj<UserService>;
    routerSpy = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should mark form as invalid if fields are empty', () => {
    expect(component.loginForm.valid).toBeFalse();
  });

  it('should log in and navigate on success', fakeAsync(() => {
    component.loginForm.setValue({
      email: 'test@email.com',
      password: 'password'
    });
    userServiceSpy.login.and.returnValue(of('Mocktoken'));

    component.onSubmit();
    tick();

    expect(userServiceSpy.setToken).toHaveBeenCalledWith('Mocktoken');
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/home']);
  }));

  it('should not submit if the form is invalid', () => {
    component.loginForm.setValue({
      email: '',
      password: ''
    });
    component.onSubmit();

    expect(userServiceSpy.login).not.toHaveBeenCalled();
  });

  it('should log error on login failure', fakeAsync(() => {
    spyOn(console, 'log');

    component.loginForm.setValue({
      email: 'test@email.com',
      password: 'wrongpassword'
    });

    userServiceSpy.login.and.returnValue(throwError(() => new Error('Unauthorized')));

    component.onSubmit();
    tick();

    expect(console.log).toHaveBeenCalledWith('login failed', jasmine.any(Error));
  }));
});
