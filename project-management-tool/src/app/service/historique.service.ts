import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Tache} from '../models/tache';
import {Historique} from '../models/historique';

@Injectable({
  providedIn: 'root'
})
export class HistoriqueService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  getHistoByTache(tacheId: number): Observable<Historique[]> {
    return this.http.get<Historique[]>(`${this.apiUrl}/histo/tache/${tacheId}`)
  }

}
