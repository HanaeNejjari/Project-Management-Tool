import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Tache} from '../models/tache';

@Injectable({
  providedIn: 'root'
})
export class TacheService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  getTachesByProjet(projetId: number): Observable<Tache[]> {
    return this.http.get<Tache[]>(`${this.apiUrl}/taches/projet/${projetId}`)
  }

  addTache(tache: Partial<Tache>, projetId: number, userId: number): Observable<Tache> {
    const params = new HttpParams()
      .set('projetId', projetId)
      .set('userId', userId);
    return this.http.post<Tache>(`${this.apiUrl}/taches`, tache, {params})
  }

  updateTache(tache: Partial<Tache>): Observable<Tache> {
    return this.http.put<Tache>(`${this.apiUrl}/taches`, tache)
  }

  assignTache(tacheId: number, userId: number): Observable<Tache> {
    const params = new HttpParams()
      .set('tacheId', tacheId)
      .set('userId', userId);
    return this.http.post<Tache>(`${this.apiUrl}/taches/assignTask`, null, {params})
  }

}
