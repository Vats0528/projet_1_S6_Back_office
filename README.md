# Projet S6 - Système de Réservation Aéroport-Hôtel (Back Office)

API REST pour la gestion des réservations de transport aéroport vers hôtel.

## 🏗️ Architecture

```
┌─────────────────┐         ┌─────────────────┐         ┌─────────────────┐
│   Frontend      │  HTTP   │   Back Office   │  JDBC   │   PostgreSQL    │
│   (Spring MVC)  │ ──────► │   (MVC Framework)│ ──────► │   Database      │
│   Port: 8888    │  JSON   │   Port: 8080    │         │   Port: 5432    │
└─────────────────┘         └─────────────────┘         └─────────────────┘
```

**Note:** Le Frontend ne doit PAS avoir d'accès direct à la base de données. Il consomme uniquement les APIs JSON du Back Office.

---

## 📋 Prérequis

| Composant | Version |
|-----------|---------|
| Java JDK | 17+ |
| Apache Tomcat | 10.x (Jakarta EE) |
| PostgreSQL | 12+ |

## 📦 Dépendances

| Dépendance | Version | Description |
|------------|---------|-------------|
| `mvc-framework` | 1.0.0 | Framework MVC léger style Spring Boot |
| `postgresql` | 42.7.1 | Driver JDBC PostgreSQL |

---

## 🗄️ Base de Données

### Configuration de connexion

Modifier `src/main/java/com/projet/config/DatabaseConnection.java` :

```java
private static final String URL = "jdbc:postgresql://localhost:5432/projet_1_s6";
private static final String USER = "postgres";
private static final String PASSWORD = "postgres";
```

### Script d'initialisation

Exécuter le fichier `TABLE_sprint_1_2026_02_06.sql` :

```bash
psql -U postgres -d projet_1_s6 -f TABLE_sprint_1_2026_02_06.sql
```

### Schéma de la base

```sql
hotel (id_hotel, nom_hotel)
client (id_client, nom_client)
reservation_client (id_reservation_client, nb_passager, date_heure_arrivee, id_hotel, id_client)
```

---

## 🚀 Déploiement

### Compilation

```bash
chmod +x build.sh
./build.sh
```

### Déploiement sur Tomcat

```bash
sudo cp projet_1_s6.war /opt/tomcat10/webapps/
```

### URL de base

```
http://localhost:8080/projet_1_s6
```

---

## 📡 API Endpoints

### Hôtels

| Méthode | URL | Description |
|---------|-----|-------------|
| `GET` | `/api/hotels` | Liste tous les hôtels |
| `GET` | `/api/hotels/{id}` | Récupère un hôtel par ID |
| `POST` | `/api/hotels` | Crée un nouvel hôtel |

**Exemple de réponse GET /api/hotels :**
```json
[
    {"idHotel": 1, "nomHotel": "Hotel Carlton"},
    {"idHotel": 2, "nomHotel": "Hotel Colbert"},
    {"idHotel": 3, "nomHotel": "Hotel Ibis"}
]
```

### Clients

| Méthode | URL | Description |
|---------|-----|-------------|
| `GET` | `/api/clients` | Liste tous les clients |
| `GET` | `/api/clients/{id}` | Récupère un client par ID |
| `POST` | `/api/clients` | Crée un nouveau client |

### Réservations

| Méthode | URL | Description |
|---------|-----|-------------|
| `GET` | `/api/reservations` | Liste toutes les réservations |
| `GET` | `/api/reservations/{id}` | Récupère une réservation par ID |
| `GET` | `/api/reservations/date/{date}` | **Filtre par date** (format: YYYY-MM-DD) |
| `POST` | `/api/reservations` | Crée une nouvelle réservation |
| `PUT` | `/api/reservations/{id}` | Met à jour une réservation |
| `DELETE` | `/api/reservations/{id}` | Supprime une réservation |

**Exemple de réponse GET /api/reservations :**
```json
[
    {
        "idReservation": 1,
        "nbPassager": 2,
        "dateHeureArrivee": "2026-02-06 14:30:00",
        "idHotel": 1,
        "idClient": 1,
        "nomHotel": "Hotel Carlton",
        "nomClient": "Jean Dupont"
    }
]
```

**Exemple de filtre par date GET /api/reservations/date/2026-02-06 :**
```json
[
    {
        "idReservation": 1,
        "nbPassager": 2,
        "dateHeureArrivee": "2026-02-06 14:30:00",
        "idHotel": 1,
        "idClient": 1,
        "nomHotel": "Hotel Carlton",
        "nomClient": "Jean Dupont"
    },
    {
        "idReservation": 2,
        "nbPassager": 4,
        "dateHeureArrivee": "2026-02-06 18:00:00",
        "idHotel": 2,
        "idClient": 2,
        "nomHotel": "Hotel Colbert",
        "nomClient": "Marie Martin"
    }
]
```

**Exemple de création POST /api/reservations :**
```json
{
    "idClient": 1,
    "nbPassager": 3,
    "dateHeureArrivee": "2026-02-10 15:00:00",
    "idHotel": 2
}
```

---

## 🔧 Guide du Framework MVC

### Annotations disponibles

#### Contrôleurs

| Annotation | Description |
|------------|-------------|
| `@Controller` | Contrôleur retournant des vues |
| `@RestController` | Contrôleur REST retournant du JSON |
| `@CrossOrigin(origins = "*")` | Active CORS |

#### Mappings HTTP

| Annotation | Description |
|------------|-------------|
| `@GetMapping("/path")` | Requêtes GET |
| `@PostMapping("/path")` | Requêtes POST |
| `@PutMapping("/path")` | Requêtes PUT |
| `@DeleteMapping("/path")` | Requêtes DELETE |

#### Paramètres

| Annotation | Description |
|------------|-------------|
| `@PathVariable("name")` | Variable du chemin URL |
| `@RequestParam("name")` | Paramètre de requête |
| `@RequestBody` | Corps JSON désérialisé |

### Classes utilitaires

```java
// Réponse OK
return ResponseEntity.ok(data);

// Réponse avec statut
return ResponseEntity.status(HttpStatus.CREATED).body(data);
return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
```

### Exemple de contrôleur

```java
@RestController
@CrossOrigin(origins = "*")
public class ExempleController {

    @GetMapping("/api/items")
    public ResponseEntity<List<Item>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/api/items/{id}")
    public ResponseEntity<Item> getById(@PathVariable("id") int id) {
        Item item = repository.findById(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(item);
    }

    @PostMapping("/api/items")
    public ResponseEntity<Item> create(@RequestBody Item item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(item));
    }
}
```

---

## 📁 Structure du projet

```
projet_1_S6_Back_office/
├── lib/
│   └── mvc-framework-1.0.0.jar
├── src/main/java/com/projet/
│   ├── config/
│   │   └── DatabaseConnection.java
│   ├── controller/
│   │   ├── HotelController.java
│   │   ├── ClientController.java
│   │   └── ReservationController.java
│   ├── model/
│   │   ├── Hotel.java
│   │   ├── Client.java
│   │   └── Reservation.java
│   └── repository/
│       ├── HotelRepository.java
│       ├── ClientRepository.java
│       └── ReservationRepository.java
├── src/main/webapp/WEB-INF/
│   └── web.xml
├── TABLE_sprint_1_2026_02_06.sql
├── build.sh
└── README.md
```

---

## 🧪 Tests avec cURL

```bash
# Liste des hôtels
curl http://localhost:8080/projet_1_s6/api/hotels

# Liste des réservations
curl http://localhost:8080/projet_1_s6/api/reservations

# Filtre par date
curl http://localhost:8080/projet_1_s6/api/reservations/date/2026-02-06

# Créer une réservation
curl -X POST http://localhost:8080/projet_1_s6/api/reservations \
  -H "Content-Type: application/json" \
  -d '{"idClient":1,"nbPassager":2,"dateHeureArrivee":"2026-02-10 14:00:00","idHotel":1}'
```

---

## 📱 Utilisation depuis le Frontend (Spring MVC)

```java
// Dans le Frontend, appeler les APIs sans accès direct à la base
RestTemplate restTemplate = new RestTemplate();

// Récupérer les hôtels
Hotel[] hotels = restTemplate.getForObject(
    "http://localhost:8080/projet_1_s6/api/hotels", 
    Hotel[].class
);

// Récupérer les réservations filtrées par date
Reservation[] reservations = restTemplate.getForObject(
    "http://localhost:8080/projet_1_s6/api/reservations/date/2026-02-06", 
    Reservation[].class
);

// Créer une réservation
Reservation newReservation = new Reservation();
newReservation.setIdClient(1);
newReservation.setNbPassager(3);
newReservation.setDateHeureArrivee("2026-02-10 15:00:00");
newReservation.setIdHotel(2);

Reservation created = restTemplate.postForObject(
    "http://localhost:8080/projet_1_s6/api/reservations",
    newReservation,
    Reservation.class
);
```
