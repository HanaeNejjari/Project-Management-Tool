import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Projet} from '../models/projet';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjetService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  getUserProjets(): Observable<Projet[]> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({Authorization: 'Bearer ' + token});

    return this.http.get<Projet[]>(`${this.apiUrl}/projets/mesprojets`, {headers});
  }

  createProjet(projet: any): Observable<Projet> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({Authorization: 'Bearer ' + token});

    return this.http.post<Projet>(`${this.apiUrl}/projets`, projet ,{headers});
  }

  getUserRole(userId: number, roleId: number): Observable<string> {
    return this.http.get(`${this.apiUrl}/projets/role/${userId}/${roleId}`, { responseType: 'text' });
  }

  updateProjet(projet: Projet): Observable<Projet> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({Authorization: 'Bearer ' + token});

    return this.http.put<Projet>(`${this.apiUrl}/projets/${projet.id}`, projet , {headers});
  }

  deleteProjet(projet:Projet): Observable<String> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({Authorization: 'Bearer ' + token});

    return this.http.delete(`${this.apiUrl}/projets/${projet.id}`, {headers, responseType: 'text' });
  }

  getUsersInprojet(projetId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/projets/${projetId}/users`);
  }

  addUserInProjet(email: string, projetId: number, role: string): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({Authorization: 'Bearer ' + token});
    const params = new HttpParams()
      .set('addEmail', email)
      .set('projetId', projetId)
      .set('role', role);
    return this.http.post(`${this.apiUrl}/projets/assignrole`, null , {headers, params});
  }

  changeUserRole(email: string, projetId: number, role: string): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({Authorization: 'Bearer ' + token});
    const params = new HttpParams()
      .set('updateEmail', email)
      .set('projetId', projetId)
      .set('role', role);

    return this.http.put(`${this.apiUrl}/projets/updaterole`, null , {headers, params});
  }

  deleteUserRole(id: number): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({Authorization: 'Bearer ' + token});
    return this.http.delete(`${this.apiUrl}/projets/removerole/${id}`, {headers, responseType: 'text'});
  }

}
