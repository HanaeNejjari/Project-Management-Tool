import { TestBed } from '@angular/core/testing';
import {CanActivateFn, Router} from '@angular/router';

import { connectedGuard } from './connected.guard';

describe('connectedGuard', () => {
  let routerSpy: jasmine.SpyObj<Router>;

  const executeGuard: CanActivateFn = (...guardParameters) =>
      TestBed.runInInjectionContext(() => connectedGuard(...guardParameters));

  beforeEach(() => {
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [{ provide: Router, useValue: routerSpy }]
    });
    spyOn(localStorage, 'getItem');
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should return false and redirect to /home if token exists', () => {
    (localStorage.getItem as jasmine.Spy).and.returnValue('fake-token');

    const result = executeGuard({} as any, {} as any);

    expect(result).toBeFalse();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/home']);
  });

  it('should return true if token does not exist', () => {
    (localStorage.getItem as jasmine.Spy).and.returnValue(null);

    const result = executeGuard({} as any, {} as any);

    expect(result).toBeTrue();
    expect(routerSpy.navigate).not.toHaveBeenCalled();
  });

});
