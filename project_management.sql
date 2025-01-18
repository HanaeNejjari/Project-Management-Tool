CREATE DATABASE project_management_tool;

USE project_management_tool;

-- Table Utilisateur
CREATE TABLE Utilisateur (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nomUtilisateur VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    motDePasse VARCHAR(255) NOT NULL
);

-- Table Projet
CREATE TABLE Projet (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    description TEXT,
    dateDebut DATE NOT NULL
);

-- Table Rôle
CREATE TABLE Rôle (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(50) NOT NULL,
    identifiantUtilisateur INTEGER NOT NULL,
    identifiantProjet INTEGER NOT NULL,
    FOREIGN KEY (identifiantUtilisateur) REFERENCES Utilisateur(id) ON DELETE CASCADE,
    FOREIGN KEY (identifiantProjet) REFERENCES Projet(id) ON DELETE CASCADE
);

-- Table Tâche
CREATE TABLE Tâche (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    description TEXT,
    dateEcheance DATE,
    priorite VARCHAR(50),
    dateFin DATE,
    statut VARCHAR(50),
    idProjet INTEGER NOT NULL,
    idUtilisateur INTEGER NOT NULL,
    FOREIGN KEY (idProjet) REFERENCES Projet(id) ON DELETE CASCADE,
    FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur(id) ON DELETE CASCADE
);

-- Table HistoriqueModifications
CREATE TABLE HistoriqueModifications (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    dateModification DATE NOT NULL,
    champsModifies VARCHAR(100),
    ancienneValeur TEXT,
    nouvelleValeur TEXT,
    idTache INTEGER NOT NULL,
    FOREIGN KEY (idTache) REFERENCES Tâche(id) ON DELETE CASCADE
);

-- Table Notification
CREATE TABLE Notification (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    contenu TEXT NOT NULL,
    dateEnvoi DATE NOT NULL,
    typeNotification VARCHAR(50),
    idUtilisateur INTEGER NOT NULL,
    FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur(id) ON DELETE CASCADE
);

