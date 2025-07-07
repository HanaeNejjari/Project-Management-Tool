export interface Historique {
  id: number;
  date_modification: Date;
  champ_modifie: string;
  ancienne_valeur: string;
  nouvelle_valeur: string;
  id_tache: number;
}
