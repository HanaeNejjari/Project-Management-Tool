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

  afterEach(() => {
    httpMock.verify();
  });

});
