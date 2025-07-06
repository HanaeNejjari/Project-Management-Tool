CREATE DATABASE project_management_tool;

USE project_management_tool;

-- Table Utilisateur
CREATE TABLE utilisateur (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nom_utilisateur VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL
);

-- Table Projet
CREATE TABLE projet (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    projet_desc TEXT,
    date_debut DATE NOT NULL
);

-- Table Rôle
CREATE TABLE role_utilisateur (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(50) NOT NULL,
    id_utilisateur INTEGER NOT NULL,
    id_projet INTEGER NOT NULL,
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id) ON DELETE CASCADE,
    FOREIGN KEY (id_projet) REFERENCES projet(id) ON DELETE CASCADE
);

-- Table Tâche
CREATE TABLE tache (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    tache_desc TEXT,
    date_echeance DATE,
    priorite VARCHAR(50),
    date_fin DATE,
    statut VARCHAR(50),
    id_projet INTEGER NOT NULL,
    id_utilisateur INTEGER NOT NULL,
    FOREIGN KEY (id_projet) REFERENCES projet(id) ON DELETE CASCADE,
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- Table HistoriqueModifications
CREATE TABLE historique_modif (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    date_modification DATE NOT NULL,
    champ_modifie VARCHAR(100),
    ancienne_valeur TEXT,
    nouvelle_valeur TEXT,
    id_tache INTEGER NOT NULL,
    FOREIGN KEY (id_tache) REFERENCES tache(id) ON DELETE CASCADE
);
