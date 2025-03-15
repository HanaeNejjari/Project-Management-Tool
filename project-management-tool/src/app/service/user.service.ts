import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) { }

  getUsers(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  isUserExist(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/exists?email=${email}`);
  }

  register(nomUtilisateur: string, email: string, motDePasse: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/register`, {nomUtilisateur, email, motDePasse});
  }

  login(email: string, motDePasse: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/login`, {email, motDePasse}, { responseType: 'text' });
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
