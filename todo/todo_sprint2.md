# Feature : Token-based access simple (Sprint 2)

TL : ETU003244 - Kiady
BO : ETU002647 - Naly
FO : ETU003330 - Vatosoa  

---

## Enoncé

Créer une fonction de génération de token, une table token, et une authentification par token avant l’accès aux endpoints via l’URL.

Format URL (exemple) :
http://localhost/main/{token_code}/endpoint

Le token est alphanumérique.  
Sans token valide, impossible d’accéder aux données des endpoints.

---

## A faire -> TL  
[Objectif-par-branche] [Sprint2/Feat] (neutre)

- [x] todo et distribution des tâches
- [x] conception du flow global token (generate → store → validate)
- [x] définition du format token (longueur, charset, unicité)
- [x] faire et commit structure BDD  
  - fichier : TABLE_token_sprint2.sql  
  - type : [feat]
- [x] commit données de base de test  
  - fichier : init-token.sql
- [x] final merge feature -> staging (repo_BO)
- [x] simulation data (token valides / invalides)
- [x] cherry-pick -> release-XX-XX-2026 (from main)
- [x] merge main -> staging / release (neutre)

---

## A faire -> BO  
[Objectif-par-endpoint]

### Base de données
- [x] création table token
  - id_token
  - token_code (alphanumérique, unique)
  - status (ACTIVE / REVOKED)
  - date_creation
  - date_expiration

---

### Token
- [x] fonction de génération de token alphanumérique
- [x] vérification unicité du token
- [x] endpoint génération token  
  POST /api/token/generate
- [x] service de validation token (interne)

---

### Sécurisation des endpoints
- [x] récupération du token depuis l’URL  
  /main/{token_code}/endpoint
- [x] vérification du token avant l’exécution du controller (uniquement reservation pour l instant )
- [x] gestion des erreurs :
  - token absent
  - token invalide
  - token révoqué
- [x] réponse HTTP 401 / 403 sans exposition de données

---

### Endpoints protégés
- [x] /api/reservation

---

## A faire -> FO  (Optionnel mais pour le visuel)
[Objectif-par-pages]

- [x] adaptation des appels API avec token dans l’URL
- [x] gestion des erreurs token côté UI
- [x] message utilisateur : accès refusé
- [x] tests avec :
  - token valide
  - token invalide
  - token absent

---

## Workflow Git

- Branche : feature/token-auth
- Merge :
  - feature -> staging
  - staging -> release
- Cherry-pick uniquement pour hotfix

---

## STATUS

liste todo : [FINISH]  
avancement : 10 / 10  

étape actuelle du workflow :
- conception
- environnement DEV
- branche feature

---

## DONE (critères de validation)

- [x] token généré automatiquement
- [x] token stocké en base
- [x] endpoint inaccessible sans token
- [x] endpoint inaccessible avec token invalide
- [x] endpoint accessible avec token valide
- [x] aucune donnée exposée sans authentification token
    
