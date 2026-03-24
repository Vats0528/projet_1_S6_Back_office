CREATE TABLE client(
   id_client SERIAL,
   nom_client VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_client)
);

CREATE TABLE lieu(
   id_lieu SERIAL,
   code VARCHAR(50) ,
   libelle VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_lieu)
);

CREATE TABLE reservation_client(
   id_reservation_client SERIAL,
   nb_passager INTEGER NOT NULL,
   date_heure_arrivee TIMESTAMP,
   id_hotel INTEGER NOT NULL,
   status VARCHAR(50) DEFAULT 'EN_ATTENTE',
   id_client INTEGER NOT NULL,
   PRIMARY KEY(id_reservation_client),
   FOREIGN KEY(id_hotel) REFERENCES lieu(id_lieu),
   FOREIGN KEY(id_client) REFERENCES client(id_client)
);

CREATE TABLE token(
   id_token SERIAL,
   niveau VARCHAR(50) ,
   date_expiration TIMESTAMP,
   PRIMARY KEY(id_token)
);

CREATE TABLE param_vehicule(
   id_param_vehicule SERIAL,
   vitess_moyenne INTEGER,
   temps_attente INTEGER,
   PRIMARY KEY(id_param_vehicule)
);

CREATE TABLE distance(
   id_distance SERIAL,
   temps TIME,
   distance NUMERIC(10,2)  ,
   id_lieu INTEGER NOT NULL,
   id_lieu_1 INTEGER NOT NULL,
   PRIMARY KEY(id_distance),
   FOREIGN KEY(id_lieu) REFERENCES lieu(id_lieu),
   FOREIGN KEY(id_lieu_1) REFERENCES lieu(id_lieu)
);

CREATE TABLE carburant(
   id_carburant INTEGER,
   nom_carburant VARCHAR(50) ,
   PRIMARY KEY(id_carburant)
);

CREATE TABLE vehicule(
   id_vehicule SERIAL,
   model VARCHAR(50) ,
   nb_place INTEGER,
   id_carburant INTEGER NOT NULL,
   PRIMARY KEY(id_vehicule),
   FOREIGN KEY(id_carburant) REFERENCES carburant(id_carburant)
);

CREATE TABLE reservation_vehicule(
   id_reservation_vehicule SERIAL,
   date_heure_depart TIMESTAMP,
   date_heure_retour TIMESTAMP,
   id_vehicule INTEGER NOT NULL,
   PRIMARY KEY(id_reservation_vehicule),
   FOREIGN KEY(id_vehicule) REFERENCES vehicule(id_vehicule)
);

CREATE TABLE details_reservation_client(
   id_reservation_client INTEGER,
   id_reservation_vehicule INTEGER,
   PRIMARY KEY(id_reservation_client, id_reservation_vehicule),
   FOREIGN KEY(id_reservation_client) REFERENCES reservation_client(id_reservation_client),
   FOREIGN KEY(id_reservation_vehicule) REFERENCES reservation_vehicule(id_reservation_vehicule)
);

CREATE TABLE details_trajet(
   id_distance INTEGER,
   id_reservation_vehicule INTEGER,
   succession INTEGER,
   PRIMARY KEY(id_distance, id_reservation_vehicule),
   FOREIGN KEY(id_distance) REFERENCES distance(id_distance),
   FOREIGN KEY(id_reservation_vehicule) REFERENCES reservation_vehicule(id_reservation_vehicule)
);
