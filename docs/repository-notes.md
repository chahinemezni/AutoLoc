# Atelier 3 – Repository

## 1. Choix de l'interface

J'ai utilisé `JpaRepository` pour les 9 repositories du projet. Cette interface permet de faire les opérations de base sur la base de données comme ajouter, rechercher, modifier et supprimer des données. Elle permet aussi de faire du tri et de la pagination.

## 2. Corrections SonarQube

### 1. Contrat.java

Sonar signalait un problème au niveau de la relation entre `Contrat` et `Paiement`. La relation `@OneToMany` ne précisait pas la colonne de jointure.

Pour corriger ça, j'ai ajouté `@JoinColumn(name = "id_contrat")` afin de préciser la clé étrangère utilisée pour cette relation.

### 2. VehiculeController.java

Sonar signalait que le contrôleur utilisait directement l'entité `Vehicule` dans les requêtes et les réponses.

J'ai créé la classe `VehiculeDto` et modifié le contrôleur pour utiliser ce DTO à la place de l'entité. Ça permet de séparer les données envoyées par l'API de celles utilisées par la base de données.

