import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {Projet} from '../../models/projet';
import {User} from '../../models/user';
import {ProjetService} from '../../service/projet.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Tache} from '../../models/tache';
import {TacheService} from '../../service/tache.service';

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

  userRole!: string;
  usersInProjet: {
    id: number;
    email: string;
    role: string;
    userId: number;
  }[] = []

  tachesList: Tache[] = [];

  roles = ['Administrateur', 'Membre', 'Observateur'];
  priorites = ['Haute', 'Moyenne', 'Basse'];
  statuts = ['Non commencé', 'En cours', 'Terminé'];

  isEditing: boolean = false;
  updatedProjet!: Projet;
  newRole = 'Observateur';
  newEmail = '';
  editedRoles : {[email: string]: string} = {};
  editedTacheId: number | null = null;
  editedTache: Partial<Tache> = {};


  constructor(private projetService: ProjetService, private tacheService: TacheService, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.getUserRole()
    this.getUsersRole()
    this.getTaches()
  }

  ngOnChanges(){
    this.getUserRole()
    this.getUsersRole()
    this.getTaches()
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
          role: item.libelle,
          userId: item.utilisateur.id
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

  getTaches(){
    if(this.projet){
      this.tacheService.getTachesByProjet(this.projet.id).subscribe({
        next: data => this.tachesList = data.map((item: any) => ({
          id: item.id,
          nom: item.nom,
          tacheDesc: item.tacheDesc,
          dateEcheance: item.dateEcheance,
          priorite: item.priorite,
          dateFin: item.dateFin,
          statut: item.statut,
          idProjet: item.projet.id,
          idUtilisateur: item.user.id,
        })),
        error: err => console.error('Erreur recupération taches', err),
      })
    }
  }

  createTache(){
    const newTache: Partial<Tache> = {
      nom : 'Nouvelle tâche',
      priorite: 'Basse',
      statut: 'Non commencé'
    }
    const user = this.usersInProjet.find(u => u.email === this.user.email);

    if (user?.userId != undefined){
      this.tacheService.addTache(newTache, this.projet.id, user.id).subscribe({
        next: () => {
          const message = 'Nouvelle tâche créée'
          this.snackBar.open(message, 'OK', {duration: 2000, panelClass: ['success-snackbar']});
          this.getTaches()
        },
        error: (error) => {
          const message = error.error.message || error.error || 'Une erreur est survenue';
          this.snackBar.open(message, 'OK', {duration: 2000, panelClass: ['error-snackbar']});
        }
      })
    }
  }

  startEditTache(tache: Tache){
    this.editedTacheId = tache.id;
    this.editedTache = {...tache};
  }

  saveTache(){
    if(this.editedTacheId != null){
      this.tacheService.updateTache(this.editedTache).subscribe({
        next: () => {
          const message = 'Tâche modifié avec succés'
          this.snackBar.open(message, 'OK', {duration: 2000, panelClass: ['success-snackbar']});
          this.getTaches()
          this.editedTacheId = null;
        },
        error: (error) => {
          const message = error.error.message || error.error || 'Une erreur est survenue';
          this.snackBar.open(message, 'OK', {duration: 2000, panelClass: ['error-snackbar']});
        }
      })
    }
  }

  assignTache(){
    if(this.editedTacheId != null && this.editedTache.idUtilisateur != undefined){
      this.tacheService.assignTache(this.editedTacheId, this.editedTache.idUtilisateur).subscribe({
        next: () => {
          const message = 'Tâche assigné avec succés'
          this.snackBar.open(message, 'OK', {duration: 2000, panelClass: ['success-snackbar']});
          this.getTaches()
        },
        error: (error) => {
          const message = error.error.message || error.error || 'Une erreur est survenue';
          this.snackBar.open(message, 'OK', {duration: 2000, panelClass: ['error-snackbar']});
        }
      })
    }
  }

  cancelEditTache(){
    this.editedTacheId = null;
  }

  getUserEmail(userId: number): String{
    const user = this.usersInProjet.find(u => u.userId === userId);
    return user ? user.email: 'Inconnu';
  }

  normalize(value: string): string {
    return value.toLowerCase().replace(/\s+/g, '-');
  }
}
