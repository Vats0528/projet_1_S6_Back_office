CREATE TABLE client(
   id_client SERIAL,
   nom_client VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_client)
);

CREATE TABLE hotel(
   id_hotel SERIAL,
   nom_hotel VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_hotel)
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
