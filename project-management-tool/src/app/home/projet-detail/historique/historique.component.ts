import {Component, Inject, inject, OnInit} from '@angular/core';
import {Historique} from '../../../models/historique';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {HistoriqueService} from '../../../service/historique.service';

@Component({
  selector: 'app-historique',
  standalone: false,

  templateUrl: './historique.component.html',
  styleUrl: './historique.component.scss'
})
export class HistoriqueComponent implements OnInit {


  historique: Historique[] = [];

  constructor(@Inject(MAT_DIALOG_DATA) public data: {tacheId : number}, private histoService: HistoriqueService) {
  }
  ngOnInit(): void {
    this.getHistorique()
  }

  getHistorique(){
    this.histoService.getHistoByTache(this.data.tacheId).subscribe({
      next: data => this.historique = data.map((item: any) => ({
        id: item.id,
        date_modification: item.dateModification,
        champ_modifie: item.champModifie,
        ancienne_valeur: item.ancienneValeur,
        nouvelle_valeur: item.nouvelleValeur,
        id_tache: item.tache.id
      })),
      error: err => console.error('Erreur recupération historique', err),
    })
  }

}
