# AutoLoc API — Documentation technique et analyse SonarQube

## A. Présentation de l'API

L'API REST AutoLoc permet de gérer les principales ressources d'une application de location de véhicules : agences, employés, véhicules, équipements, clients, réservations, contrats, paiements et maintenances.

La documentation interactive est générée avec SpringDoc et OpenAPI.

- **URL de base :** `http://localhost:8081/api`
- **Swagger UI :** `http://localhost:8081/api/swagger-ui.html`
- **Documentation OpenAPI JSON :** `http://localhost:8081/api/v3/api-docs`

## B. Endpoints REST

| Ressource | Endpoint | Méthodes HTTP |
|---|---|---|
| Agence | `/agences` | GET, POST, PUT, DELETE |
| Employé | `/employes` | GET, POST, PUT, DELETE |
| Véhicule | `/vehicules` | GET, POST, PUT, DELETE |
| Équipement | `/equipements` | GET, POST, PUT, DELETE |
| Client | `/clients` | GET, POST, PUT, DELETE |
| Réservation | `/reservations` | GET, POST, PUT, DELETE |
| Contrat | `/contrats` | GET, POST, PUT, DELETE |
| Paiement | `/paiements` | GET |
| Maintenance | `/maintenances` | GET, POST, PUT, DELETE |

Les méthodes indiquées correspondent aux opérations prévues dans les contrôleurs. Les paiements sont exposés en lecture seule.

## C. Gestion des relations JSON

Les annotations Jackson permettent de gérer les relations entre les entités et d'éviter les références circulaires lors de la sérialisation JSON.

- `@JsonManagedReference("contrat-paiements")` est utilisée dans l'entité `Contrat` sur la liste des paiements.
- `@JsonBackReference("contrat-paiements")` est utilisée dans l'entité `Paiement` sur la référence vers le contrat.
- `@JsonIgnore` est utilisée sur certaines relations bidirectionnelles pour empêcher la sérialisation récursive.

La relation entre `Contrat` et `Paiement` utilise `cascade = CascadeType.ALL`. Le comportement de création, de mise à jour et de suppression des paiements associés doit être vérifié à l'aide de tests.

## D. Analyse SonarQube

### 1. Périmètre de l'analyse

L'analyse statique a été réalisée sur le fichier `ContratController.java` avec SonarQube for IDE.

### 2. Problèmes identifiés

#### Problème 1 — java:S4684

- **Emplacement initial :** ligne 46, colonne 47.
- **Problème :** remplacer cette entité de persistance par un objet POJO simple ou un DTO.
- **Explication :** le contrôleur expose directement une entité JPA dans l'API REST.
- **Impact :** cela couple l'API à la structure de persistance et peut exposer des informations internes de l'entité.
- **Recommandation :** utiliser un DTO pour définir les données exposées par l'API.

#### Problème 2 — java:S4684

- **Emplacement initial :** ligne 54, colonne 33.
- **Problème :** remplacer cette entité de persistance par un objet POJO simple ou un DTO.
- **Explication :** le contrôleur accepte ou retourne directement une entité de persistance.
- **Impact :** cela couple le contrat de l'API au modèle de données.
- **Recommandation :** utiliser des DTO de requête et de réponse pour cet endpoint.

### 3. Conclusion de l'analyse

SonarQube a identifié deux problèmes liés à l'exposition directe des entités de persistance dans le contrôleur REST. L'utilisation de DTO permet de séparer le modèle de données de la représentation exposée par l'API.

Le contrôleur `ContratController` utilise `ContratDto` pour recevoir et retourner les données des contrats. Une nouvelle analyse SonarQube doit être effectuée pour vérifier si les deux problèmes ont été corrigés.

## E. Tests Postman et scénario de cascade Contrat/Paiement

### 1. Tests de l'API avec Postman

Les tests suivants doivent être réalisés :

- **GET de toutes les ressources :** réponse attendue `200 OK`.
- **GET d'une ressource existante par ID :** réponse attendue `200 OK`.
- **GET d'une ressource inexistante :** réponse attendue `404 Not Found`.
- **POST d'une ressource valide :** réponse attendue `201 Created`.
- **POST de données invalides :** réponse attendue `400 Bad Request`.
- **PUT d'une ressource existante :** réponse attendue `200 OK`.
- **DELETE d'une ressource existante :** réponse attendue `204 No Content`.
- **POST `/api/paiements` :** réponse attendue `405 Method Not Allowed`, puisque le contrôleur des paiements est en lecture seule.

### 2. Tests de cascade Contrat/Paiement

Les trois scénarios suivants doivent être vérifiés :

1. **Création :** créer un contrat contenant plusieurs paiements et vérifier que le contrat et ses paiements sont enregistrés en base de données.
2. **Mise à jour :** modifier un contrat et vérifier que ses paiements sont conservés conformément au comportement attendu.
3. **Suppression :** supprimer un contrat et vérifier le comportement des paiements qui lui sont associés.

Pour chaque scénario, il faut vérifier le code HTTP retourné et l'état des données persistées en base. Un test ne doit être déclaré réussi qu'après vérification de son résultat.

### 3. Vérification finale

Avant la livraison du projet, il faut vérifier que :

- l'application compile correctement ;
- l'application démarre sans erreur ;
- les tests Postman donnent les résultats attendus ;
- le comportement de cascade Contrat/Paiement est vérifié ;
- les corrections SonarQube sont confirmées par une nouvelle analyse.
