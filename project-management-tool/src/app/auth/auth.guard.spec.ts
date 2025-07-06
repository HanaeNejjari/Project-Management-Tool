import { TestBed } from '@angular/core/testing';
import {CanActivateFn, Router} from '@angular/router';

import { authGuard } from './auth.guard';

describe('authGuard', () => {
  let routerSpy: jasmine.SpyObj<Router>;

  const executeGuard: CanActivateFn = (...guardParameters) =>
      TestBed.runInInjectionContext(() => authGuard(...guardParameters));

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

  it('should return true if token exists', () => {
    (localStorage.getItem as jasmine.Spy).and.returnValue('Mocktoken');

    const result = executeGuard({} as any, {} as any);

    expect(result).toBeTrue();
    expect(routerSpy.navigate).not.toHaveBeenCalled();
  });

  it('should return false and navigate if token does not exist', () => {
    (localStorage.getItem as jasmine.Spy).and.returnValue(null);

    const result = executeGuard({} as any, {} as any);

    expect(result).toBeFalse();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/auth/choice']);
  });

});
