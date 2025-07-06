import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {Projet} from '../../models/projet';
import {User} from '../../models/user';
import {ProjetService} from '../../service/projet.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-projet-detail',
  standalone: false,

  templateUrl: './projet-detail.component.html',
  styleUrl: './projet-detail.component.scss'
})
export class ProjetDetailComponent implements OnInit, OnChanges {
  @Input() user !: User;
  @Input() projet!: Projet;
  @Output() projetChanged = new EventEmitter<Projet>();

  isEditing: boolean = false;
  updatedProjet!: Projet;
  userRole!: string;
  usersInProjet: {
    id: number;
    email: string;
    role: string;
  }[] = []
  roles = ['Administrateur', 'Membre', 'Observateur'];
  newRole = 'Observateur';
  newEmail = '';
  editedRoles : {[email: string]: string} = {};

  constructor(private projetService: ProjetService, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.getUserRole()
    this.getUsersRole()
  }

  ngOnChanges(){
    this.getUserRole()
    this.getUsersRole()
  }

  getUserRole(){
    //On récupére le role de l'utilisateur connecté
    if(this.projet  && this.user) {
      this.projetService.getUserRole(this.user.id, this.projet.id).subscribe((role) => {
        this.userRole = role;
      })
    }
  }

  getUsersRole(){
    //On récupére les roles de tous les utilisateurs du projet
    if(this.projet) {
      this.projetService.getUsersInprojet(this.projet.id).subscribe(data => {
        this.usersInProjet = data.map((item: any) => ({
          id: item.id,
          email: item.utilisateur.email,
          role: item.libelle
        }));
      })
    }
  }

  startEdit(){
    //Début de l'edition, on fait une copie du projet
    this.isEditing = true;
    this.updatedProjet = {...this.projet};
    //On récupére les roles actuels pour les afficher
    this.editedRoles = {};
    this.usersInProjet.forEach(user => {
      this.editedRoles[user.email] = user.role;
    })
  }

  saveEdit(){
    //Sauvegarde du projet en bdd
    this.projetService.updateProjet(this.updatedProjet).subscribe({
      next: (projet) => {
        this.projet = projet;
        this.isEditing = false;
        this.snackBar.open('Projet modifié avec succés', 'OK', {duration: 2000, panelClass: ['success-snackbar']});
        this.projetChanged.emit(projet)
      },
      error: (error) => {
        const message = error.error.message || error.error || 'Une erreur est survenue';
        this.snackBar.open(message, 'OK', {duration: 2000, panelClass: ['error-snackbar']});
      }
    })
  }

  cancelEdit(){
    //Quitte l'edition sans sauvegarde
    this.isEditing = false;
  }

  addUserInProjet(){
    this.projetService.addUserInProjet(this.newEmail, this.projet.id, this.newRole).subscribe({
      next: () => {
        const message = 'Role ajouté avec succés'
        this.snackBar.open(message, 'OK', {duration: 2000, panelClass: ['success-snackbar']});
        this.getUsersRole();
        this.editedRoles[this.newEmail] = this.newRole;
        this.newEmail = '';
        this.newRole = 'Observateur';
      },
      error: (error) => {
        console.log(error);
        const message = error.error.message || error.error || 'Une erreur est survenue';
        this.snackBar.open(message, 'OK', {duration: 2000, panelClass: ['error-snackbar']});
      }
    })
  }

  changeRole(changeEmail: string){
    this.projetService.changeUserRole(changeEmail, this.projet.id, this.editedRoles[changeEmail]).subscribe({
      next: () => {
        const message = 'Role modifié avec succés'
        this.snackBar.open(message, 'OK', {duration: 2000, panelClass: ['success-snackbar']});
        this.getUsersRole();
      },
      error: (error) => {
        const message = error.error.message || error.error || 'Une erreur est survenue';
        this.snackBar.open(message, 'OK', {duration: 2000, panelClass: ['error-snackbar']});
      }
    });
  }

  deleteUserInProjet(deleteEmail: string){
    let deleteUser = this.usersInProjet.find(user => user.email === deleteEmail);
    if (deleteUser) {
      this.projetService.deleteUserRole(deleteUser.id).subscribe({
        next: () => {
          const message = 'Role supprimé avec succés'
          this.snackBar.open(message, 'OK', {duration: 2000, panelClass: ['success-snackbar']});
          this.getUsersRole();
        },
        error: (error) => {
          const message = error.error.message || error.error || 'Une erreur est survenue';
          this.snackBar.open(message, 'OK', {duration: 2000, panelClass: ['error-snackbar']});
        }
      })
    }

  }


}
