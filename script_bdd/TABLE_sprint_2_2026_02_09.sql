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
   id_lieu INTEGER NOT NULL,
   id_client INTEGER NOT NULL,
   PRIMARY KEY(id_reservation_client),
   FOREIGN KEY(id_lieu) REFERENCES lieu(id_lieu),
   FOREIGN KEY(id_client) REFERENCES client(id_client)
);

CREATE TABLE token(
   id_token SERIAL,
   niveau VARCHAR(50) ,
   date_expiration TIMESTAMP,
   PRIMARY KEY(id_token)
);

