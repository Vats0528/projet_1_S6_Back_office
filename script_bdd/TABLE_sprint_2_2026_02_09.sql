alter table hotel rename to lieu;
alter table lieu  rename column nom to libelle;
alter table lieu rename column id_hotel to id_lieu;

CREATE TABLE token(
   id_token SERIAL,
   niveau VARCHAR(50) ,
   date_expiration TIMESTAMP,
   PRIMARY KEY(id_token)
);