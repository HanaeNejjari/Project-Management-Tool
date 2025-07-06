import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {User} from '../../models/user';
import {Projet} from '../../models/projet';
import {ProjetService} from '../../service/projet.service';

@Component({
  selector: 'app-projet-list',
  standalone: false,

  templateUrl: './projet-list.component.html',
  styleUrl: './projet-list.component.scss'
})
export class ProjetListComponent implements OnInit, OnChanges {

  @Input() user!: User;
  @Input() projets: Projet[] = [];
  @Output() projetSelected = new EventEmitter<Projet>();


  constructor(private projetService: ProjetService) {}

  ngOnInit(): void {
    this.getProjets();
  }

  ngOnChanges(): void {
    this.getProjets();
  }

  getProjets(): void{
    console.log(this.user)
    if (this.user){
      this.projetService.getUserProjets().subscribe({
        next: data => this.projets = data,
        error: err => console.error('Erreur recupération projets', err),
      })
    }
  }

  selectProjet(projet: Projet): void {
    console.log(projet);
    this.projetSelected.emit(projet)
  }

  createProjet(): void {
    const projet: any = {
      nom: "Nouveau Projet",
      projetDesc: "Description projet",
      dateDebut: new Date()
    }

    this.projetService.createProjet(projet).subscribe((projet) => {
      this.selectProjet(projet)
      this.getProjets()
    })

  }


}
