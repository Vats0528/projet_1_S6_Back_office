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
- [ ] conception du flow global token (generate → store → validate)
- [ ] définition du format token (longueur, charset, unicité)
- [ ] faire et commit structure BDD  
  - fichier : TABLE_token_sprint2.sql  
  - type : [feat]
- [ ] commit données de base de test  
  - fichier : init-token.sql
- [ ] final merge feature -> staging (repo_BO)
- [ ] simulation data (token valides / invalides)
- [ ] cherry-pick -> release-XX-XX-2026 (from main)
- [ ] merge main -> staging / release (neutre)

---

## A faire -> BO  
[Objectif-par-endpoint]

### Base de données
- [x] création table token
  - id_token
  - token_code (alphanumérique, unique)
  - status (ACTIVE / REVOKED)
  - created_at
  - expired_at 

---

### Token
- [ ] fonction de génération de token alphanumérique
- [ ] vérification unicité du token
- [ ] endpoint génération token  
  POST /api/token/generate
- [ ] service de validation token (interne)

---

### Sécurisation des endpoints
- [ ] récupération du token depuis l’URL  
  /main/{token_code}/endpoint
- [ ] vérification du token avant l’exécution du controller
- [ ] gestion des erreurs :
  - token absent
  - token invalide
  - token révoqué
- [ ] réponse HTTP 401 / 403 sans exposition de données

---

### Endpoints protégés
- [ ] /api/client
- [ ] /api/hotel
- [ ] /api/reservation

---

## A faire -> FO  (Optionnel mais pour le visuel)
[Objectif-par-pages]

- [ ] adaptation des appels API avec token dans l’URL
- [ ] gestion des erreurs token côté UI
- [ ] message utilisateur : accès refusé
- [ ] tests avec :
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

liste todo : [EN_ATTENTE]  
avancement : 0 / 100  

étape actuelle du workflow :
- conception
- environnement DEV
- branche feature

---

## DONE (critères de validation)

- [ ] token généré automatiquement
- [ ] token stocké en base
- [ ] endpoint inaccessible sans token
- [ ] endpoint inaccessible avec token invalide
- [ ] endpoint accessible avec token valide
- [ ] aucune donnée exposée sans authentification token
    
