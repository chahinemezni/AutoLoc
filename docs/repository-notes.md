# Atelier 4 – Couche Service

## A. Injection par constructeur et par attribut

| Injection par constructeur | Injection par attribut |
|---|---|
| Dépendances visibles | Dépendances moins visibles |
| Permet d'utiliser `final` | Ne permet pas d'utiliser `final` sur le champ injecté |
| Facilite les tests | Rend les tests plus difficiles |
| Recommandée par Spring | Moins recommandée |

## B. Injection des dépendances

Les neuf services utilisent `@Service`, `@RequiredArgsConstructor` et un repository déclaré avec `private final`.

| Service | Repository |
|---|---|
| AgenceServiceImpl | IAgenceRepository |
| EmployeServiceImpl | IEmployeRepository |
| VehiculeServiceImpl | IVehiculeRepository |
| EquipementServiceImpl | IEquipementRepository |
| ClientServiceImpl | IClientRepository |
| ReservationServiceImpl | IReservationRepository |
| ContratServiceImpl | IContratRepository |
| PaiementServiceImpl | IPaiementRepository |
| MaintenanceServiceImpl | IMaintenanceRepository |

## C. Erreurs Spring

1. **Bean introuvable :** vérifier `@Service` et le package de la classe.
2. **Plusieurs beans :** utiliser `@Primary` ou `@Qualifier`.
3. **Dépendance circulaire :** modifier la conception pour supprimer la dépendance entre les deux services.

## D. Qualité du code et SonarQube

L'analyse SonarQube n'a affiché aucun problème, aucun Security Hotspot et aucune vulnérabilité liée aux données.

Pour améliorer la qualité du code, les services utilisent l'injection par constructeur et `ResourceNotFoundException` pour gérer les ressources introuvables. Le contrôleur utilise également `VehiculeDto` au lieu d'exposer directement l'entité.

## E. Questions

**1. Qui crée `ContratServiceImpl` ?**

Spring crée le service au démarrage grâce à `@Service`.

**2. Pourquoi utiliser `IContratService` ?**

Pour réduire le couplage et faciliter les tests.

**3. Pourquoi un service singleton doit-il rester sans état ?**

Pour éviter que plusieurs requêtes partagent des données temporaires.

**4. Pourquoi charger l'entité avant de la modifier ?**

Pour vérifier qu'elle existe et modifier uniquement les champs nécessaires.

**5. Différences entre les annotations :**

- `@Component` : détecte une classe comme bean Spring.
- `@Bean` : déclare un bean à partir d'une méthode.
- `@Primary` : choisit un bean par défaut.
- `@Qualifier` : choisit un bean précis.

`IPaiementService` contient seulement `findById()` et `findAll()`, car les paiements sont gérés à travers les contrats.