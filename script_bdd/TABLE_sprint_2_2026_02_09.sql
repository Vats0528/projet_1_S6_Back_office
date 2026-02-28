alter table hotel rename to lieu;
alter table lieu  rename column nom to libelle;
alter table lieu rename column id_hotel to id_lieu;

CREATE TABLE token(
   id_token SERIAL,
   niveau VARCHAR(50) ,
   date_expiration TIMESTAMP,
   PRIMARY KEY(id_token)
);
CREATE TABLE hotel(
   id_hotel SERIAL,
   nom_hotel VARCHAR(50) ,
   id_lieu INTEGER NOT NULL,
   PRIMARY KEY(id_hotel),
   FOREIGN KEY(id_lieu) REFERENCES lieu(id_lieu)
);