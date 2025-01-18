INSERT INTO Utilisateur (nomUtilisateur, email, motDePasse) VALUES
('Leonardo Dicaprio', 'leonardo.dicaprio@example.com', 'hashed_password1'),
('Brad Pitt', 'brad.pitt@example.com', 'hashed_password2'),
('Angelina Jolie', 'angelina.jolie@example.com', 'hashed_password3'),
('Jennifer Aniston', 'jennifer.aniston@example.com', 'hashed_password4');


INSERT INTO Projet (nom, description, dateDebut) VALUES
('Gestion de concours de Noel', 'Mise en place du concours annuel de Noel.', '2024-08-01'),
('Mise à jour site', 'Mise à jour du site web de l\'entreprise.', '2024-02-15'),
('Collaboration avec les bureaux de Paris', 'Développement d\'une application mobile de suivi commune avec les bureaux parisiens.', '2024-10-12');


INSERT INTO Rôle (libelle, identifiantUtilisateur, identifiantProjet) VALUES
('Administrateur', 1, 1),
('Membre', 2, 1),
('Observateur', 3, 1);


INSERT INTO Tâche (nom, description, dateEcheance, priorite, dateFin, statut, idProjet, idUtilisateur) VALUES
('Créer le schéma UML', 'Créer le schéma UML du projet.', '2024-01-10', 'Haute', NULL, 'En cours', 1, 1),
('Mettre en place la base de données', 'Créer les tables et relations.', '2024-12-15', 'Haute', NULL, 'Non commencé', 1, 2),
('Design des maquettes', 'Créer les maquettes de l\'interface utilisateur.', '2025-01-20', 'Moyenne', NULL, 'En attente', 1, 3),
('Entrer en contact avec les developpeurs', 'Entrer en contact avec les developpeurs.', '2025-02-10', 'Basse', NULL, 'Non commencé', 2, 4);


INSERT INTO HistoriqueModifications (dateModification, champsModifies, ancienneValeur, nouvelleValeur, idTache) VALUES
('2024-12-05', 'statut', 'Non commencé', 'En cours', 1),
('2024-12-12', 'dateEcheance', '2024-01-15', '2024-01-18', 2),
('2024-12-15', 'priorite', 'Moyenne', 'Haute', 3);


INSERT INTO Notification (contenu, dateEnvoi, typeNotification, idUtilisateur) VALUES
('Votre tâche "Créer le schéma UML" a été mise à jour.', '2024-01-05', 'Mise à jour tâche', 1),
('La tâche "Mettre en place la base de données" a été assignée à vous.', '2024-01-06', 'Attribution tâche', 2),
('La date d\'échéance de la tâche "Entrer en contact avec les developpeurs" a changé.', '2024-01-12', 'Modification échéance', 3);



