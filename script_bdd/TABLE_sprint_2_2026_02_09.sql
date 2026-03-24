alter table hotel rename to lieu;
alter table lieu  rename column nom_hotel to libelle;
alter table lieu rename column id_hotel to id_lieu;
ALTER TABLE lieu
ADD COLUMN code VARCHAR(50);

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
