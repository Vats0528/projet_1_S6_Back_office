alter table hotel rename to lieu;
alter table lieu  rename column nom to libelle;
alter table lieu rename column id_hotel to id_lieu;

CREATE TABLE param_vehicule(
   id_param_vehicule SERIAL,
   vitess_moyenne INTEGER,
   temps_attente INTEGER,
   PRIMARY KEY(id_param_vehicule)
);
CREATE TABLE vehicule(
   id_vehicule SERIAL,
   model VARCHAR(50) ,
   nb_place INTEGER,
   id_carburant INTEGER NOT NULL,
   PRIMARY KEY(id_vehicule),
   FOREIGN KEY(id_carburant) REFERENCES carburant(id_carburant)
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
CREATE TABLE reservation_vehicule(
   id_reservation_vehicule SERIAL,
   date_heure_depart TIMESTAMP,
   date_heure_retour TIMESTAMP,
   PRIMARY KEY(id_reservation_vehicule)
);

CREATE TABLE details_trajet(
   id_distance INTEGER,
   id_reservation_vehicule INTEGER,
   succession INTEGER,
   PRIMARY KEY(id_distance, id_reservation_vehicule),
   FOREIGN KEY(id_distance) REFERENCES distance(id_distance),
   FOREIGN KEY(id_reservation_vehicule) REFERENCES reservation_vehicule(id_reservation_vehicule)
);
