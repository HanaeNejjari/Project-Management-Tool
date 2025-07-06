import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { RegisterComponent } from './register.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatInput} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIcon} from '@angular/material/icon';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {UserService} from '../../service/user.service';
import {Router} from '@angular/router';
import {of, throwError} from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let userServiceSpy: jasmine.SpyObj<UserService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const userServiceMock = jasmine.createSpyObj('UserService', ['isUserExist', 'register', 'setToken']);
    const routerMock = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
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

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    userServiceSpy = TestBed.inject(UserService) as jasmine.SpyObj<UserService>;
    routerSpy = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should show error if passwords do not match', () => {
    component.registerForm.setValue({
      userName: 'test',
      email: 'test@email.com',
      password: 'password',
      confirmPassword: 'wrongpassword'
    });

    component.onSubmit();

    const confirmPasswordErrors = component.registerForm.get('confirmPassword')?.errors;
    expect(confirmPasswordErrors).toEqual({ passwordsNotMatching: true });
  });

  it('should show error message if user already exists', fakeAsync(() => {
    component.registerForm.setValue({
      userName: 'test',
      email: 'test@email.com',
      password: 'password',
      confirmPassword: 'password'
    });

    userServiceSpy.isUserExist.and.returnValue(of(true));

    component.onSubmit();
    tick();

    expect(component.errorMessage).toBe('Un utilisateur avec cet email existe déjà !');
  }));

  it('should invalidate the form when fields are empty', () => {
    component.registerForm.setValue({ userName: '', email: '', password: '', confirmPassword: '' });
    expect(component.registerForm.valid).toBeFalsy();
  });

  it('should not submit if the form is invalid', () => {
    component.registerForm.setValue({
      userName: '',
      email: '',
      password: '',
      confirmPassword: ''
    });

    component.onSubmit();

    expect(userServiceSpy.register).not.toHaveBeenCalled();
  });

  it('should register user and navigate if valid', fakeAsync(() => {
    component.registerForm.setValue({
      userName: 'test',
      email: 'test@email.com',
      password: 'password',
      confirmPassword: 'password'
    });

    userServiceSpy.isUserExist.and.returnValue(of(false));
    userServiceSpy.register.and.returnValue(of('mockToken'));

    component.onSubmit();
    tick();

    expect(userServiceSpy.setToken).toHaveBeenCalledWith('mockToken');
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/home']);
  }));

  it('should log error if register fails', fakeAsync(() => {
    spyOn(console, 'log');
    component.registerForm.setValue({
      userName: 'test',
      email: 'test@email.com',
      password: 'password',
      confirmPassword: 'password'
    });

    userServiceSpy.isUserExist.and.returnValue(of(false));
    userServiceSpy.register.and.returnValue(throwError(() => new Error('fail')));

    component.onSubmit();
    tick();

    expect(console.log).toHaveBeenCalledWith('register failed', jasmine.any(Error));
  }));

});
