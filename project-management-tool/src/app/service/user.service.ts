import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  getUsers(): Observable<any> {
    return this.http.get(`${this.apiUrl}/users`);
  }

  isUserExist(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/users/exists?email=${email}`);
  }

  register(nomUtilisateur: string, email: string, motDePasse: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/users/register`, {nomUtilisateur, email, motDePasse}, { responseType: 'text' });
  }

  login(email: string, motDePasse: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/users/login`, {email, motDePasse}, { responseType: 'text' });
  }

  setToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUserInfo(): any{
    const token = this.getToken();
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return {
        email: payload.sub,
        userId: payload.userId
      }
    }
    return null;
  }

  isLoggedIn(): boolean{
    return !!this.getToken();
  }

  logout(): void {
    localStorage.removeItem('token');
  }
}
