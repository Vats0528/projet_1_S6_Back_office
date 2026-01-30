# Projet 1 S6 - API Backend

API REST pour exposer les données de la base de données PostgreSQL, utilisant le **MVC Framework v1.0**.

## Prérequis

- **Java JDK 17** ou supérieur
- **Apache Tomcat 10.x** (Jakarta EE 9+)
- **PostgreSQL 12** ou supérieur

## Dépendances

| Dépendance | Version | Description |
|------------|---------|-------------|
| `mvc-framework` | 1.0.0 | Framework MVC léger style Spring Boot |
| `postgresql` | 42.7.1 | Driver JDBC PostgreSQL |

Les JARs sont dans le dossier `lib/` et `build/WEB-INF/lib/`.

---

## Guide du Framework MVC

### Annotations disponibles

#### Contrôleurs

| Annotation | Description |
|------------|-------------|
| `@Controller` | Marque une classe comme contrôleur (retourne des vues) |
| `@RestController` | Marque une classe comme contrôleur REST (retourne du JSON) |
| `@CrossOrigin(origins = "...")` | Active CORS pour le contrôleur |

#### Mappings HTTP

| Annotation | Description |
|------------|-------------|
| `@GetMapping("/path")` | Gère les requêtes GET |
| `@PostMapping("/path")` | Gère les requêtes POST |
| `@PutMapping("/path")` | Gère les requêtes PUT |
| `@DeleteMapping("/path")` | Gère les requêtes DELETE |
| `@RequestMapping("/path")` | Mapping générique |

#### Paramètres

| Annotation | Description |
|------------|-------------|
| `@PathVariable("name")` | Extrait une variable du chemin URL |
| `@RequestParam("name")` | Extrait un paramètre de requête |
| `@RequestBody` | Désérialise le corps JSON en objet |
| `@RequestHeader("name")` | Extrait un header HTTP |
| `@Session` | Accède à la session utilisateur |

#### Sécurité

| Annotation | Description |
|------------|-------------|
| `@Authenticated` | Requiert une authentification |
| `@RequiresRole("role")` | Requiert un rôle spécifique |
| `@AllowAnonymous` | Autorise l'accès sans authentification |

#### Réponses

| Annotation | Description |
|------------|-------------|
| `@ResponseStatus(HttpStatus.OK)` | Définit le code HTTP de réponse |

### Classes utilitaires

#### ResponseEntity
```java
// Réponse OK avec données
return ResponseEntity.ok(data);

// Réponse avec statut personnalisé
return ResponseEntity.status(HttpStatus.CREATED).body(data);

// Réponse sans contenu
return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
```

#### HttpStatus
```java
HttpStatus.OK                    // 200
HttpStatus.CREATED               // 201
HttpStatus.NO_CONTENT            // 204
HttpStatus.BAD_REQUEST           // 400
HttpStatus.UNAUTHORIZED          // 401
HttpStatus.FORBIDDEN             // 403
HttpStatus.NOT_FOUND             // 404
HttpStatus.INTERNAL_SERVER_ERROR // 500
```

### Exemple de contrôleur complet

```java
package com.projet.controller;

import com.framework.annotation.*;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;

@RestController
@CrossOrigin(origins = "*")
public class ExempleController {

    // GET /api/items
    @GetMapping("/api/items")
    public ResponseEntity<List<Item>> getAll() {
        List<Item> items = repository.findAll();
        return ResponseEntity.ok(items);
    }

    // GET /api/items/5
    @GetMapping("/api/items/{id}")
    public ResponseEntity<Item> getById(@PathVariable("id") int id) {
        Item item = repository.findById(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(item);
    }

    // POST /api/items
    @PostMapping("/api/items")
    public ResponseEntity<Item> create(@RequestBody Item item) {
        Item created = repository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/items/5
    @PutMapping("/api/items/{id}")
    public ResponseEntity<Item> update(
            @PathVariable("id") int id,
            @RequestBody Item item) {
        item.setId(id);
        Item updated = repository.update(item);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/items/5
    @DeleteMapping("/api/items/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        repository.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    // GET /api/items/search?name=test
    @GetMapping("/api/items/search")
    public ResponseEntity<List<Item>> search(@RequestParam("name") String name) {
        List<Item> items = repository.findByName(name);
        return ResponseEntity.ok(items);
    }
}
```

---

## Configuration du projet

```sql
CREATE DATABASE projet_1_s6;
\c projet_1_s6;

CREATE TABLE test(
    id INT PRIMARY KEY,
    text TEXT NOT NULL,
    date DATE NOT NULL
);

INSERT INTO test VALUES (1, 'Hello world', '2026-01-30');
```

## Configuration de la connexion

Modifier le fichier `src/main/java/com/projet/config/DatabaseConnection.java` si nécessaire :

```java
private static final String URL = "jdbc:postgresql://localhost:5432/projet_1_s6";
private static final String USER = "postgres";
private static final String PASSWORD = "postgres";
```

## Compilation et Déploiement

### Option 1 : Script de build (Linux/Mac)

```bash
chmod +x build.sh
./build.sh
```

### Option 2 : Compilation manuelle

```bash
# Créer les répertoires
mkdir -p build/WEB-INF/classes
mkdir -p build/WEB-INF/lib

# Copier les fichiers
cp -r src/main/webapp/* build/
cp lib/*.jar build/WEB-INF/lib/

# Compiler
javac -cp "lib/*:build/WEB-INF/lib/*" -d build/WEB-INF/classes $(find src/main/java -name "*.java")

# Créer le WAR
cd build && jar -cvf ../projet_1_s6.war .
```

### Déploiement sur Tomcat

1. Copiez `projet_1_s6.war` dans `$TOMCAT_HOME/webapps/`
2. Démarrez Tomcat
3. L'API sera accessible à `http://localhost:8080/projet_1_s6/api/tests`

## Endpoints API

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/api/tests` | Liste tous les enregistrements |
| GET | `/api/tests/{id}` | Récupère un enregistrement par ID |

## Exemple de réponse

### GET /api/tests

```json
[
    {
        "id": 1,
        "text": "Hello world",
        "date": "2026-01-30"
    }
]
```

### GET /api/tests/1

```json
{
    "id": 1,
    "text": "Hello world",
    "date": "2026-01-30"
}
```

## Utilisation depuis le Frontend

```javascript
// Exemple avec fetch
fetch('http://localhost:8080/projet_1_s6/api/tests')
    .then(response => response.json())
    .then(data => console.log(data));

// Exemple avec axios
axios.get('http://localhost:8080/projet_1_s6/api/tests')
    .then(response => console.log(response.data));
```

## Structure du projet

```
projet_1_S6_Back_office/
├── lib/
│   └── mvc-framework-1.0.0.jar
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── projet/
│       │           ├── config/
│       │           │   └── DatabaseConnection.java
│       │           ├── controller/
│       │           │   └── TestController.java
│       │           ├── model/
│       │           │   └── Test.java
│       │           └── repository/
│       │               └── TestRepository.java
│       └── webapp/
│           └── WEB-INF/
│               └── web.xml
├── build.sh
└── README.md
```

## CORS

L'API est configurée pour accepter les requêtes de n'importe quelle origine (`@CrossOrigin(origins = "*")`). En production, modifiez cette configuration pour n'accepter que les origines autorisées.
