import {Component, OnInit} from '@angular/core';
import {UserService} from '../service/user.service';
import {Router} from '@angular/router';
import {Projet} from '../models/projet';
import {ProjetService} from '../service/projet.service';
import {User} from '../models/user';

@Component({
  selector: 'app-home',
  standalone: false,

  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  userInfo!: User;
  projets: Projet[] = [];
  selectedProjet!: Projet;


  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.userService.getCurrentUserInfo().subscribe(data => {
      this.userInfo = data;
    })
  }

  onProjetSelected(projet: Projet) {
    this.selectedProjet = projet;
  }

  logout(): void {
    console.log('logout');
    this.userService.logout();
    this.router.navigate(['/auth/choice']);
  }

  onProjetChanged(projetMaj: Projet) {
    this.projets = this.projets.map(p => p.id === projetMaj.id ? projetMaj : p);
    this.selectedProjet = projetMaj;
  }

}
