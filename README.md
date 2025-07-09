# ProjectManagementTool

Ce projet a été réalisé dans le cadre d'une étude de cas en tant que développeur full-stack chez "Code Solutions". Il s'agit d'une plateforme collaborative de gestion de projets nommée Project Management Tool (PMT).

Le but principal était de permettre à des équipes de développement logiciel de planifier, suivre et organiser leurs projets de manière fluide et simplifiée. Ce projet m'a été confié par John Doe (CEO), encadré par Nicolas (Product Owner) pour les besoins fonctionnels, et par Mariana (Tech Lead) pour les aspects techniques.

L'application repose sur un frontend Angular et un backend Spring Boot, avec une base de données relationnelle. Il met en œuvre des fonctionnalités essentielles comme l’authentification JWT, la gestion de projets, la répartition de rôles, les tâches et leur suivi historique. Elle a été conçue dans une optique d’industrialisation : dockerisation, tests automatisés et déploiement via une pipeline GitHub Actions.

## Environnement
Le projet est dévéloppé avec MySql Angular et Springboot. Voici les versions des outils qui ont été utilisés :

`Java 21.0.6`
`Maven 3.9.9`
`Node.js 22.12.0`
`Angular CLI 19.0.6`
`MySQL 8.0.41`
`Docker 28.3.0`

## Base de données
### Schéma de la base de données
<img src="./screen\schema_bdd.png" alt="Schéma bdd">

## Lancement du projet en local
### Lancement de la BDD
Dans MySqlWorkbench créer une connexion comme celle-ci:
<img src="./screen\connexion_bdd.png" alt="Connexion bdd">
* Hostname : localhost
* Port: 3306
* Username : root
* Password : Mama010166.

Exécuter les deux fichiers :
* project_management.sql
* donnees_test.sql

Vérifier que la base de données est bien créé:
<img src="./screen\bdd.png" alt="bdd">

### Lancement du backend
Ouvrer un terminal dans le répertoire './backend/project-management-tool'
Puis taper la commande:
```bash
   mvn spring-boot:run  
```
Le backend va se lancer sur http://localhost:8080

L'api est documentée grâce à Swagger UI et est accessible via le lien suivant :
http://localhost:8080/swagger-ui/index.html

<img src="./screen\swagger.png" alt="swagger">

### Lancement du frontend
Ouvrer un terminal dans le repertoire './project-management-tool'

Puis taper la commande:
```bash
   ng serve  
```
Ouvrer sur un navigateur le lien http://localhost:4200 pour accéder au projet

## Project Management Tool
En arrivant sur le site l'utilisateur a le choix de créer un nouveau compte ou de se connecter sur un compte existant:
<img src="./screen\pmt_choice.png" alt="choice">

Une fois connecté l'utilisateur peut naviguer entre ses projets pour les afficher, visualiser les tâches et modifier les données en fonction de son rôle dans le projet
<img src="./screen\pmt_projet.png" alt="projet">
<img src="./screen\pmt_modif.png" alt="modif projet">

## Test
### Test backend
Ouvrer un terminal dans le répertoire './backend/project-management-tool'
Puis taper les commandes:
```bash
   mvn clean test  
   mvn jacoco:report
```

Le rapport de coverage est disponible dans le repertoire './backend/project-management-tool/target/site/jacoco/index.html'
<img src="./screen\test_back.png" alt="test backend">

### Test frontend
Ouvrer un terminal dans le répertoire './project-management-tool'
Puis taper la commande:
```bash
    ng test --code-coverage
```

Le rapport de coverage est disponible dans le repertoire './project-management-tool/coverage/project-management-too/index.html'
<img src="./screen\test_front.png" alt="test frontend">

### Docker
Lancer Docker Desktop
Ouvrir un terminal depuis la racine du projet et taper la commande:

```bash
   docker-compose up --build
```

Les conteneurs sont créés et l'application est accessible via le liens suivant : http://localhost:4200/index.html
<img src="./screen\docker.png" alt="docker">
<img src="./screen\projet_docker.png" alt="Projet sous docker">
