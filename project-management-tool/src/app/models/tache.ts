export interface Tache {
  id: number;
  nom: string;
  tacheDesc: string;
  dateEcheance: Date;
  priorite: string;
  dateFin: Date;
  statut: string;
  idProjet: number;
  idUtilisateur: number;
}
