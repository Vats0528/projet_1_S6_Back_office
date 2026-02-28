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
   id_client INTEGER NOT NULL,
   PRIMARY KEY(id_reservation_client),
   FOREIGN KEY(id_hotel) REFERENCES hotel(id_hotel),
   FOREIGN KEY(id_client) REFERENCES client(id_client)
);


CREATE TABLE token(
   id_token SERIAL,
   niveau VARCHAR(50) ,
   valeur_token VARCHAR(250) ,
   date_creation TIMESTAMP,
   date_expiration TIMESTAMP,
   est_actif BOOLEAN,
   PRIMARY KEY(id_token)
);

CREATE TABLE hotel(
   id_hotel SERIAL,
   nom_hotel VARCHAR(50) ,
   id_lieu INTEGER NOT NULL,
   PRIMARY KEY(id_hotel),
   FOREIGN KEY(id_lieu) REFERENCES lieu(id_lieu)
);
