import { TestBed } from '@angular/core/testing';

import { UserService } from './user.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {environment} from '../../environments/environment';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch users', () => {
    const dummyUsers = [{ email: 'test@email.com' }];
    service.getUsers().subscribe(users => {
      expect(users).toEqual(dummyUsers);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/users`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyUsers);
  });

  it('should check if user exists', () => {
    const mockEmail = 'test@email.com';
    service.isUserExist(mockEmail).subscribe(exists => {
      expect(exists).toBeTrue();
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/users/exists?email=${mockEmail}`);
    expect(req.request.method).toBe('GET');
    req.flush(true);
  });

  it('should be register an user and return a token', () => {
    const mockUsername = 'test';
    const mockEmail= 'test@email.com';
    const mockPassword= 'password';
    const mockToken = 'mockToken';

    service.register(mockUsername, mockEmail, mockPassword).subscribe( token => {
      expect(token).toBe(mockToken);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/users/register`);
    expect(req.request.method).toBe('POST');
    req.flush(mockToken);
  });

  it('should be login an user and return a token', () => {
    const mockEmail= 'test@email.com';
    const mockPassword= 'password';
    const mockToken = 'mockToken';
    service.login(mockEmail, mockPassword).subscribe(token => {
      expect(token).toBe(mockToken);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/users/login`);
    expect(req.request.method).toBe('POST');
    req.flush(mockToken);
  });

  it('should store and retrieve token', () => {
    const mockToken = 'mockToken';
    service.setToken(mockToken);
    expect(service.getToken()).toBe(mockToken);
  });

  it('should be return user info from token', () => {
    const payload = { sub: 'test@email.com', userId: 123 };
    const fakeToken = `header.${btoa(JSON.stringify(payload))}.signature`;
    service.setToken(fakeToken);
    const userInfo = service.getUserInfo();
    expect(userInfo).toEqual({ email: 'test@email.com', userId: 123 });
  });

  it('should return null if no token', () => {
    localStorage.clear();
    expect(service.getUserInfo()).toBeNull();
  });

  it('should return true if logged in', () => {
    service.setToken('token');
    expect(service.isLoggedIn()).toBeTrue();
  });

  it('should return false if not logged in', () => {
    localStorage.clear();
    expect(service.isLoggedIn()).toBeFalse();
  });

  it('should remove token on logout', () => {
    service.setToken('token');
    service.logout();
    expect(service.getToken()).toBeNull();
  });

  afterEach(() => {
    httpMock.verify();
  });

});
